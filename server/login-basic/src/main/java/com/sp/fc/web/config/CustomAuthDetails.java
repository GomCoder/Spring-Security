package com.sp.fc.web.config;

//AuthenticationDetailsSource를 핸들링해보기 - 잘 알아두면 유용할 것임

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class CustomAuthDetails implements AuthenticationDetailsSource<HttpServletRequest, RequestInfo> {

    /**
     * Details에 대한 정보 제공
     * @param request the request object, which may be used by the authentication details
     * object
     * @return
     */
    @Override
    public RequestInfo buildDetails(HttpServletRequest request) {
        return RequestInfo.builder()
                .remoteIp(request.getRemoteAddr())
                .sessionId(request.getSession().getId())
                .loginTime(LocalDateTime.now()) //로그인한 당시의 시간
                .build();
    }
}
