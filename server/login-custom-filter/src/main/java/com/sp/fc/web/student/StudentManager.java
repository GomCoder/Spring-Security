package com.sp.fc.web.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

/**
 * 통행증(인증 토큰)을 발급할 Provider
 */
@Component
public class StudentManager implements AuthenticationProvider, InitializingBean {

    /**
     * 본래 DB를 통해서 가져와야 하지만 테스트이기 때문에 HashMap으로 구현함
     */
    private HashMap<String, Student> studentDB = new HashMap<>();

    /**
     * UsernamePasswordAuthenticationToken을 대상으로 해서 토큰을 발행
     * 현재 Authentication으로 입력된 토큰은 UsernamePasswordAuthenticationToken이 된다.
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        StudentAuthenticationToken token = (StudentAuthenticationToken)  authentication;
        if (studentDB.containsKey(token.getCredentials())) { //token.getName()이 StudentDB에 있는 아이디와 같다면
            Student student = studentDB.get(token.getCredentials()); //Student를 가져옴
            return StudentAuthenticationToken.builder() //토큰을 StudentAuthenticationToken으로 만들어서 전달해줌
                    .principal(student)
                    .details(student.getUsername())
                    .authenticated(true)
                    .authorities(student.getRole())
                    .build();
        }
        return null; //없다면 내가 처리할 수 없는 authentication은 null로 넘김
    }

    /**
     * UsernamePasswordAuthenticationFilter를 통해서 토큰을 받을 것이기 때문에
     * UsernamePasswordAuthenticationToken 클래스 형태의 토큰을 받으면 우리가 검증해 주는 Provider로 동작함을 선언
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == StudentAuthenticationToken.class;
    }

    /**
     * Bean이 초기화 되었을 때, studentDB에 push할 데이터, 학생 3명
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT"))),
                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")))

        ).forEach(s ->
                studentDB.put(s.getId(), s)
        );
    }
}
