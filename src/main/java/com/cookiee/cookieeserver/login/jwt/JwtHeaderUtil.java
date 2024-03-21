package com.cookiee.cookieeserver.login.jwt;

import com.cookiee.cookieeserver.global.exception.GeneralException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.cookiee.cookieeserver.global.Constant.*;
import static com.cookiee.cookieeserver.global.ErrorCode.*;

public class JwtHeaderUtil {
    /**
     * 액세스 토큰 가져오기
     * @return
     */
    public static String getAccessToken() {
        return getToken(HEADER_AUTHORIZATION);
    }

    /**
     * 리프레쉬 토큰 가져오기
     * @return
     */
    public static String getRefreshToken() {
        return getToken(HEADER_REFRESH_TOKEN);
    }

    /**
     * JWT에 넣어놓은 데이터, 즉 토큰을 가져오는 메소드
     * @param tokenHeader
     * @return
     */
    private static String getToken(String tokenHeader) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String headerValue = request.getHeader(tokenHeader);
        if (headerValue == null) throw new GeneralException(NULL_AUTHORIZATION_HEADER);

        // Bearer로 시작하고 토큰이 맞는지 확인
        if (StringUtils.hasText(headerValue) && headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());  // Bearer 뒤 내용(즉, 실제 토큰 내용) 리턴
        }
        return null;
    }
}
