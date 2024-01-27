package com.sp.fc.web.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * Student 통행증이 발급될 Student 대상을 Principal 만든다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    private String id;
    private String username;
    /**
     * Authentication 인증을 하려면 GrantedAuthority가 필요하고
     * Student는 ROLE_STUDENT 라는 Authority를 가지고 있어야 이 페이지에 겁근할 수 있다.
     */
    private Set<GrantedAuthority> role; //도메인에서 Principal이 되는 것

}
