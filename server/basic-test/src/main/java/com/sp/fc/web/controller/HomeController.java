package com.sp.fc.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping("/")
    public String index() {
        return "홈페이지";
    }

    //접근한 사용자가 어떤 권한과 어떤 인증으로 접근했는지 확인
    @RequestMapping("/auth")
    public Authentication auth() {
        return SecurityContextHolder.getContext()
                .getAuthentication(); //인증과정이 어떻게 만들어지고 있는지를 볼 수 있음
    }

    //개인정보에 해당하는 리소스를 시뮬레이션 해보기
    //User가 접근할 수 있는 페이지
    @PreAuthorize("hasAnyAuthority('ROLE_USER')") //User 권한이 있어야만 접근할 수 있음
    @RequestMapping("/user")
    public SecurityMessage user() {
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("User 정보")
                .build();
    }

    //Admin이 접근할 수 있는 페이지
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')") //Admin 권한이 있어야만 접근할 수 있음
    @RequestMapping("/admin")
    public SecurityMessage admin() {
        return SecurityMessage.builder()
                .auth(SecurityContextHolder.getContext().getAuthentication())
                .message("Admin 정보")
                .build();
    }
}
