package com.sp.fc.web.teacher;

import com.sp.fc.web.student.Student;
import com.sp.fc.web.student.StudentAuthenticationToken;
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
public class TeacherManager implements AuthenticationProvider, InitializingBean {

    TeacherAuthenticationToken teacherAuthenticationToken;
    /**
     * 본래 DB를 통해서 가져와야 하지만 테스트이기 때문에 HashMap으로 구현함
     */
    private HashMap<String, Teacher> teacherDB = new HashMap<>();

    /**
     * UsernamePasswordAuthenticationToken을 대상으로 해서 토큰을 발행
     * 현재 Authentication으로 입력된 토큰은 UsernamePasswordAuthenticationToken이 된다.
     * @param authentication the authentication request object.
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
        if (teacherDB.containsKey(token.getCredentials())) { //token.getName()이 StudentDB에 있는 아이디와 같다면
            Teacher teacher = teacherDB.get(token.getCredentials()); //Teacher를 가져옴
            return TeacherAuthenticationToken.builder() //토큰을 StudentAuthenticationToken으로 만들어서 전달해줌
                    .principal(teacher)
                    .details(teacher.getUsername())
                    .authenticated(true)
                    //.authorities(teacher.getRole())
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
        return authentication == TeacherAuthenticationToken.class;
    }

    /**
     * Bean이 초기화 되었을 때, studentDB에 push할 데이터, 학생 3명
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("choi", "최선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        ).forEach(s ->
                teacherDB.put(s.getId(), s)
        );
    }
}