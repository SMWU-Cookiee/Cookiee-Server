package com.cookiee.cookieeserver.login.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

// 이 엔트리포인트는 스프링 시큐리티에서 인증과 관련된 예외가 발생했을 때 이를 처리하는 로직을 담당
// 인증되지 않은 사용자(로그인 되지 않은)가 보호된 리소스에 접근하려고 시도하면 실행되는 예외 처리
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    // 여기서 HandlerExceptionResolver의 빈을 주입받고 있는데, HandlerExceptionResolver의 빈이 두 종류가 있기 때문에
    // @Qualifier로 handlerExceptionResolver를 주입받을 것이라고 명시해줘야 한다.
    public JwtAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    // commence()에서 스프링 시큐리티의 인증 관련 예외를 처리
    // @ControllerAdvice에서 모든 예외를 처리하여 응답할 것이기 때문에 여기에 별다른 로직은 작성하지 않고 HandlerExceptionResolver에 예외 처리를 위임
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
//        //resolver.resolveException(request, response, null, authException);
//        resolver.resolveException(request, response, null, (Exception) request.getAttribute("exception"));
//    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
