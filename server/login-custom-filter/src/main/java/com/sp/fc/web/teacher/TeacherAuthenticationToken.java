package com.sp.fc.web.teacher;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 도메인에 Teacher가 사용할 수 있는 통행증
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherAuthenticationToken implements Authentication {

    //lombok에 의해 Getter가 자동 생성되므로 선언한 부분
    private Teacher principal;
    private String credentials;
    private String details;
    private boolean authenticated; //인증 도장을 박을 장소
    //private Set<GrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //Teacher가 되는 principal이 Granted Authority를 가지고 있기 때문에
        //principal이 null -> 빈 객체를 넘겨줌
        //있다면 getRole()을 넘겨줌
        return principal == null ?  new HashSet<>() : principal.getRole();
    }


    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }
}