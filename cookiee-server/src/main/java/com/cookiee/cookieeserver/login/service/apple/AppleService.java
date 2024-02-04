package com.cookiee.cookieeserver.login.service.apple;

import com.cookiee.cookieeserver.login.controller.apple.AppleClient;
import com.cookiee.cookieeserver.domain.AuthProvider;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.login.dto.response.ApplePublicKeyResponse;
import com.cookiee.cookieeserver.login.dto.response.AppleTokenResponse;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.simple.parser.ParseException;
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
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

//@Slf4j
@RequiredArgsConstructor
@Service
public class AppleService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppleClient appleClient;

    @Value("${apple.team.id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.login-key}")
    private String APPLE_LOGIN_KEY;

    @Getter
    @Value("${apple.client.id}")
    private String APPLE_CLIENT_ID;

    @Value("${apple.redirect.url}")
    private String APPLE_REDIRECT_URL;

    @Value("${apple.key.path}")
    private String APPLE_KEY_PATH;

    private final static String APPLE_AUTH_URL = "https://appleid.apple.com";

    // 애플 로그인 url 만드는 메소드
    public String getAppleLoginUrl() {
        return APPLE_AUTH_URL + "/auth/authorize"
                + "?client_id=" + APPLE_CLIENT_ID
                + "&redirect_uri=" + APPLE_REDIRECT_URL
                + "&response_type=code%20id_token&scope=name%20email&response_mode=form_post";
    }

    // 애플로부터 받은 authentication code를 사용하여 auth token을 발급받고 유저 정보를 읽어온다.
    // 읽어온 정보가 기존 DB에 저장되어 있지 않다면 회원가입을 위해 새로 저장해주고, 그렇지 않다면 로그인을 수행한다.
    // 두 경우 모두 JWT access token과 refresh token을 발급해준다.
    @Transactional
    public User login(String idToken, String authorizationCode) {
        String socialId;
        String email;
        String refreshToken;

        User user;

        // public key 구성요소를 조회한 뒤 JWT의 서명을 검증한 후 Claim을 응답
        // identity token의 payload들이 인코딩 되어 claims에 있음. -> 디코딩하여 apple 고유 계정 id 등 중요 요소를 획득해서 사용하면 된다.
        Claims claims = getClaimsBy(idToken);

        try {
            //JSONParser jsonParser = new JSONParser();
            // 생성한 auth token을 파싱
            //JSONObject jsonObj = (JSONObject) jsonParser.parse(generateAuthToken(authorizationCode));

            // access token 등.. 생성해서 그 내용을 appleTokenResponse에 받아온다.
            AppleTokenResponse appleTokenResponse = generateAuthToken(authorizationCode);
            refreshToken = appleTokenResponse.getRefreshToken();
            //refreshToken = String.valueOf(jsonObj.get("refresh_token"));

            // ID TOKEN을 통해 회원 고유 식별자 받기
            // SignedJWT signedJWT = SignedJWT.parse(String.valueOf(jsonObj.get("id_token")));

//            SignedJWT signedJWT = SignedJWT.parse(appleTokenResponse.getIdToken());
//            JWTClaimsSet getPayload = signedJWT.getJWTClaimsSet();
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JSONObject payload = objectMapper.readValue(getPayload.toJSONObject().toString(), JSONObject.class);

            socialId = String.valueOf(claims.get("sub"));  // sub는 애플에서 제공하는 사용자 식별 값
            email = String.valueOf(claims.get("email"));

            // userId = String.valueOf(payload.get("sub"));
            //email = String.valueOf(payload.get("email"));

            // 로그인 요청하면서 받아온 소셜 아이디와 해당 소셜 로그인 타입의 조합으로 유저 찾아오기 -> 없으면 null(새로운 사용자임)
            User findUser = userRepository
                    .findByAuthProviderAndSocialId(AuthProvider.APPLE, socialId)
                    .orElse(null);

            // 신규 회원가입인 경우
            if (findUser == null) {
                // 신규 회원가입의 경우 DB에 저장
                user = userRepository.save(
                        User.builder()
                                .socialLoginType(AuthProvider.APPLE)
                                .socialId(socialId)
                                .email(email)
                                .refreshToken(refreshToken)
                                .build()
                );
            }
            // 기존 회원인 경우 -> refresh token 업데이트 위해 DB에 저장
            else {
                findUser.setRefreshToken(refreshToken);
                user = userRepository.save(findUser);
            }

            loginSuccess(user, response);
            return user;

        } catch (ParseException | JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json data");
        } catch (IOException | java.text.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 또는 회원가입이 정상적으로 완료된 경우 토큰을 발급하고, response 헤더에 설정한다.
    public void loginSuccess(User user, HttpServletResponse response) {
        String accessToken = jwtService.createAccessToken(user.getSocialId());
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);
    }

    // public key 조회 -> JWT 서명 검증 -> claim 응답
    public Claims getClaimsBy(String identityToken){
        try {
            // 1. public key 조회
            ApplePublicKeyResponse response = appleClient.getAppleAuthPublicKey();

            String headerOfIdentityToken = identityToken.substring(0, identityToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);
            ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

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
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(identityToken).getBody();

        } catch (NoSuchAlgorithmException e) {

        } catch (InvalidKeySpecException e) {

        } catch ( MalformedJwtException e) {
            //토큰 서명 검증 or 구조 문제 (Invalid token)
            new IllegalAccessException("유효하지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            new IllegalAccessException("만료된 토큰입니다.");
        } catch (Exception e) {

        }
    }

    /**
     * APP에서 session을 유지하기위해 필요한 token을 발급 받는 메소드
     * 요청에 필요한 요소들:
     * code(Authorization Code) : APP으로 부터 넘겨받은 Authorization Code
     * client_id : App Bundle ID
     * client_secret : JWT 형식의 토큰으로 만들어야 함
     * grant_type : "authorization_code" 값을 주면 됨.
     *
     * @param code
     * @return
     * @throws IOException
     */
    public AppleTokenResponse generateAuthToken(String code) throws IOException {

        if (code == null) throw new IllegalArgumentException("Failed to get authorization code");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", APPLE_CLIENT_ID);
        params.add("client_secret", createClientSecretKey());  // public key를 받기 위해서는 client secret key 생성
        params.add("code", code);
        params.add("redirect_uri", APPLE_REDIRECT_URL);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // https://appleid.apple.com/auth/token 에 POST 요청을 보내서 액세스 토큰 등을 받아온다.
        // 성공하면
        // access_token, token_type, expires_in, refresh_token, id_token 이 담긴 json을 응답으로 받음.

//        AppleTokenResponse tokenResponse = appleClient.getToken(AppleTokenRequest.of(
//                code, APPLE_CLIENT_ID,createClientSecretKey(), "authorization_code", ));
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    APPLE_AUTH_URL + "/auth/token",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );

            // 응답으로 받은 json 데이터를 AppleTokenResponse로 변환해서 리턴
            return new ObjectMapper().readValue(response.getBody(), AppleTokenResponse.class);
        } catch (HttpClientErrorException e) {
            throw new IllegalArgumentException("Apple Auth Token Error");
        }
    }

    // 애플의 client secret을 얻기 위해 사용하는 메소드이다.
    public String createClientSecretKey() throws IOException {
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

    // 애플의 client secret을 얻기 위해 사용하는 메소드이다. key 받아오는 메소드
    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(APPLE_KEY_PATH);
        String privateKey = new String(resource.getInputStream().readAllBytes());

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

        return converter.getPrivateKey(object);
    }

}