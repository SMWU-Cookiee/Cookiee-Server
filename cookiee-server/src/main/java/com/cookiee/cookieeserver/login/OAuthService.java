package com.cookiee.cookieeserver.login;

import com.cookiee.cookieeserver.event.service.S3Uploader;
import com.cookiee.cookieeserver.global.domain.AuthProvider;
import com.cookiee.cookieeserver.global.domain.Role;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.login.apple.service.AppleService;
import com.cookiee.cookieeserver.login.dto.request.UserSignupRequestDto;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final AppleService appleService;
    private final S3Uploader s3Uploader;
    private final JwtService jwtService;

    /**
     * 회원가입
     * @param signupUserInfo
     * @return
     * @throws Exception
     */
    @Transactional
    public OAuthResponse signup(UserSignupRequestDto signupUserInfo) {
        Optional<User> foundUser = userRepository.findBySocialId(signupUserInfo.getSocialId());

        // 이미 유저가 존재하는 경우
        if(foundUser.isPresent()){
            log.debug("OAuthService, signup - 이미 존재하는 사용자");
            throw new GeneralException(USER_EXISTS);
        }

        // 그게 아니면 새로운 유저 생성
        User newUser = User.builder()
                .role(Role.USER)
                .nickname(signupUserInfo.getNickname())
                .selfDescription(signupUserInfo.getSelfDescription())
                .email(signupUserInfo.getEmail())
                .socialId(signupUserInfo.getSocialId())
                .socialLoginType(AuthProvider.of(signupUserInfo.getSocialLoginType()))
                .socialRefreshToken(signupUserInfo.getSocialRefreshToken())
                .build();

        String storedFileName;
        // 프로필 이미지 s3에 생성 후 저장된 파일명 가져오기
        storedFileName = s3Uploader.saveFile(signupUserInfo.getProfileImage(),
                    String.valueOf(newUser.getUserId()),
                    "profile");

        newUser.setProfileImage(storedFileName);

        // 리프레쉬 토큰 먼저 생성, 저장
        String refreshToken = jwtService.createRefreshToken();
        newUser.setRefreshToken(refreshToken);

        // 유저 저장
        userRepository.save(newUser);

        // 액세스 토큰 생성
        String accessToken = jwtService.createAccessToken(newUser.getUserId());
        log.debug("app refresh token: {}", refreshToken);
        log.debug("app access token: {}", accessToken);

        return OAuthResponse.builder()
                .isNewMember(false)
                .userId(newUser.getUserId())  // 나머지 api 접근에는 유저 아이디가 필요함
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 회원 탈퇴
     * @param userId
     */
    @Transactional
    public void signout(final Long userId) throws IOException {
        final User user = userRepository.findByUserId(userId).orElse(null);

        if(user == null){
            throw new GeneralException(USER_NOT_FOUND);
        }

        // 애플 로그인한 유저라면 다시 애플 서버에 요청해야 함
        if (user.getSocialLoginType().equals(AuthProvider.APPLE)) {
            appleService.revoke(user.getRefreshToken());
        }

        userRepository.delete(user);
    }
}
