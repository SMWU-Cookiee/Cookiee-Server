package com.cookiee.cookieeserver.login.apple.service;

import com.cookiee.cookieeserver.global.exception.handler.AppleAuthException;
import com.cookiee.cookieeserver.login.apple.controller.AppleClient;
import com.cookiee.cookieeserver.global.domain.AuthProvider;
import com.cookiee.cookieeserver.login.OAuthResponse;
import com.cookiee.cookieeserver.login.apple.dto.request.AppleTokenRequest;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.login.apple.dto.response.ApplePublicKeyResponse;
import com.cookiee.cookieeserver.login.apple.dto.response.AppleTokenResponse;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppleService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppleClient appleClient;

    @Value("${apple.team.id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.login.key}")
    private String APPLE_LOGIN_KEY;

    @Getter
    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.redirect.url}")
    private String APPLE_REDIRECT_URL;

    @Value("${apple.key.path}")
    private String APPLE_KEY_PATH;

    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 클라이언트에서 받은 authenticationCode를 사용해 auth token을 발급받고, 유저 정보를 받아온다.
     * 이후, 이미 존재하는 사요자라면 회원가입을 위해 새로 저장하고, 그렇지 않다면 로그인을 수행해야 한다.
     * 두 경우 모두 JWT access token과 refresh token을 발급해준다.
     * @param idToken               클라이언트에서 받은 identityToken
     * @param authorizationCode     클라이언트에서 받은 authorizationCode
     * @return                      새로운 사용자에 관한 정보
     */
    @Transactional
    public OAuthResponse login(String idToken, String authorizationCode) {
        String socialId;
        String email;
        String appleRefreshToken;

        // 1. idToken 사용 -> public key 조회한 뒤 JWT의 서명을 검증한 후 Claim을 응답
        // identity token의 payload들이 인코딩 되어 claims에 있음. -> 디코딩하여 apple 고유 계정 id 등 중요 요소를 획득해서 사용하면 된다.
        Claims claims = verifyIdentityToken(idToken);

        try {
            log.info("AppleService 로그인 시작");

            // 2. authorizationCode 사용 -> access token 등.. 생성해서 그 내용을 appleTokenResponse에 받아온다.
            AppleTokenResponse appleTokenResponse = generateAuthToken(authorizationCode);
            appleRefreshToken = appleTokenResponse.getRefreshToken();
            log.debug("애플 로그인 - 애플 고유 리프레쉬 토큰은 {}", appleRefreshToken);

            socialId = String.valueOf(claims.get("sub"));  // sub는 애플에서 제공하는 사용자 식별 값
            email = String.valueOf(claims.get("email"));

            // 로그인 요청하면서 받아온 소셜 아이디와 해당 소셜 로그인 타입의 조합으로 유저 찾아오기 -> 없으면 null(새로운 사용자임)
            User foundUser = userRepository
                    .findBySocialLoginTypeAndSocialId(AuthProvider.APPLE, socialId)
                    .orElse(null);

            /* 신규 회원가입인 경우 -> 관련 정보 리턴 */
            if (foundUser == null) {
                log.debug("socialId가 {}인 유저는 존재하지 않음. 신규 회원가입", socialId);
                return OAuthResponse.builder()
                        .socialId(socialId)
                        .socialType("apple")
                        .email(email)
                        .isNewMember(true)
                        .refreshToken(appleRefreshToken)
                        .build();
            }

            /* 기존 회원인 경우 */
            // 앱의 리프레쉬 토큰과 액세스 토큰 생성
            log.debug("socialId가 {}인 유저는 기존 유저입니다.", socialId);
            String appRefreshToken = jwtService.createRefreshToken();
            String appAccessToken = jwtService.createAccessToken(foundUser.getUserId());

            // 서버에서 만든 리프레쉬 토큰으로 저장
            foundUser.setRefreshToken(appRefreshToken);
            userRepository.save(foundUser);

            // 회원 정보 응답 (기존 회원)
            return OAuthResponse.builder()
                    .socialId(socialId)
                    .email(email)
                    .isNewMember(false)
                    .socialType("apple")
                    .accessToken(appAccessToken)
                    .refreshToken(appRefreshToken)
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json data");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * public key 조회 -> JWT 서명 검증 -> claim 응답
     * @param identityToken     클라이언트에서 보낸 identityToken
     * @return
     */
    public Claims verifyIdentityToken(String identityToken) {
        try {
            // 1. public key 조회
            ApplePublicKeyResponse response = appleClient.getAppleAuthPublicKey();
            log.debug("public key 조회 완료, public keys: {}", response.getKeys());

            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));

            Map<String, String> header = objectMapper.readValue(
                    new String(Base64.getDecoder().decode(headerOfIdentityToken), StandardCharsets.UTF_8),
                    Map.class);

            ApplePublicKeyResponse.Key key = response
                    .getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new AppleAuthException(FAILED_TO_GET_APPLE_PUBLIC_KEY));
            log.debug("apple public key 가져오기 완료, key: {}", key);

            // 응답받은 n, e 값은 base64 url-safe로 인코딩 되어 있기 때문에 반드시 디코딩하고나서 public key로 만들어야 한다.
            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // 이후 성공적으로 서명이 검증됐다면 Identity token의 payload 들이 신뢰할 수 있는 값들이라는 증명이 완료된것이다.
            // 인코딩 되어있는 payload를 디코딩하여 apple 고유 계정 id 등 중요 요소를 획득해 사용하면된다.
            // 그리고 필요에 따라 iss, aud 등 나머지 값들을 추가적으로 검증하면 된다.
            return Jwts.parserBuilder().
                    setSigningKey(publicKey).
                    build().
                    parseClaimsJws(identityToken).
                    getBody();
        } catch (MalformedJwtException e) {
            throw new AppleAuthException(MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AppleAuthException(EXPIRED_TOKEN);
        } catch(UnsupportedJwtException | IllegalArgumentException e) {
            throw new AppleAuthException(INVALID_TOKEN);
        } catch (Exception e) {
            throw new AppleAuthException(FAILED_TO_GET_APPLE_PUBLIC_KEY);
        }
    }

    /**
     * APP에서 session을 유지하기위해 필요한 token을 발급 받는 메소드 (refreshToken 등을 발급받음)
     * 요청에 필요한 요소들:
     * code(Authorization Code) : APP으로부터 넘겨받은 Authorization Code
     * client_id : App Bundle ID (중요)
     * client_secret : JWT 형식의 토큰으로 만들어야 함
     * grant_type : "authorization_code" 값을 주면 됨.
     *
     * @param code              클라이언트에서 전달받은 authorizationCode
     * @return                  애플에서 받아온 토큰 등 정보들
     * @throws IOException
     */
    public AppleTokenResponse generateAuthToken(String code) throws IOException {

        if (code == null) throw new AppleAuthException(NULL_AUTHENTICATION_CODE);

        // 애플에 보내 요청값 만들기
        AppleTokenRequest request = AppleTokenRequest.builder()
                .client_id(APPLE_CLIENT_ID)
                .client_secret(createClientSecretKey())
                .code(code)
                .grant_type("authorization_code")
                .build();

        try {
            // 애플에 요청 보내기
            return appleClient.getAppleToken(request);
        } catch (HttpClientErrorException e) {
            throw new AppleAuthException(FAILED_TO_GET_APPLE_TOKEN);
        }
    }

    /**
     * 애플에서 토큰을 받아오기 위해 보내야할 client secret을 얻어오는 메소드
     * @return      JWT Payload
     */
    public String createClientSecretKey() {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        // headerParams 적재
        // 1. JWT header 생성
        Map<String, Object> headerParamsMap = new HashMap<>();
        headerParamsMap.put("kid", APPLE_LOGIN_KEY);  // 개발자 계정으로 만든 private key를 위한 identifier
        headerParamsMap.put("alg", "ES256");  // alg는 토큰을 sign(정상적으로 발급)하기 위해 사용될 알고리즘. 계정을 위해서는 ES256 사용한다

        // clientSecretKey 생성
        // 2. JWT payload 생성
        // The JWT payload contains information specific to the Account and Organizational Data Sharing REST API
        // and the client app, such as the issuer, subject, and expiration time.
        // Use the following claims in the payload:
        return Jwts.builder()
                .setHeaderParams(headerParamsMap)  // 헤더 세팅
                .setIssuer(APPLE_TEAM_ID)  // client secret을 이슈한 사람
                .setIssuedAt(new Date(System.currentTimeMillis()))  // client secret을 생성한 시간
                .setExpiration(expirationDate) // client secret 만료 시간 (30초)
                .setAudience(APPLE_AUTH_URL)  // client secret을 받을 곳
                .setSubject(APPLE_CLIENT_ID)  // client secret 주인
                .signWith(getPrivateKey())  // 3. Sign the JWT. (SignatureAlgorithm.ES256 는 파라미터로 안 받음..)
                .compact();
    }

    /**
     * 애플의 client secret을 만들 때 사용되는 메소드, 이때 static에 있는 .p8 키 파일이 사용됨
     * @return          Private Key
     */
    private PrivateKey getPrivateKey() {
        try {
            ClassPathResource resource = new ClassPathResource(APPLE_KEY_PATH);
            String privateKey = new String(resource.getInputStream().readAllBytes());

            Reader pemReader = new StringReader(privateKey);
            PEMParser pemParser = new PEMParser(pemReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

            return converter.getPrivateKey(object);
        }
        catch(IOException e){
            throw new AppleAuthException(FAILED_TO_GET_APPLE_PRIVATE_KEY);
        }
    }

    /**
     * 애플 로그인한 유저 탈퇴
     * @param appleRefreshToken
     */
    public void revoke(String appleRefreshToken){
        // 헤더에 넣을 파라미터 값 만들기
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", APPLE_CLIENT_ID);
        params.add("client_secret", createClientSecretKey());
        params.add("token", appleRefreshToken);
        params.add("token_type_hint", "refresh_token");

        // TODO: FeignClient 방식으로 변경하기
        // 통신할 rest template 만들기
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // 애플 서버에 회원 탈퇴 요청 보내기 (revoke token)
        restTemplate.postForEntity(APPLE_AUTH_URL + "/auth/revoke", httpEntity, String.class);
    }

}