package com.sp.fc.web.config;

/**
 * Basic Token Authentication 테스트
 * HomeController의 greeting()이 반환하는 메시지가 Secure 할 때
 */

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    BasicAuthenticationFilter filter;

    /**
     * BasicAuthenticationFilter 따라가 보기
     * BasicAuthenticationFilter에서 doFilterInternal()을 통해서 인증이 이루어진 것
     * Debug 모드에서 request의 getHeaderNames()를 보면 여러 헤더 중 authorization header에 Basic Token이  넘어 온 것을 확인할 수 있다
     * 이 Basic Token을 doFilterInternal에서 BasicAuthenticationConverter의 convert()쪽에서 캐치함
     * authorization header 값을 읽어 trim()을 한 다음, Basic Token인지를 확인하고
     * Basic Token이라면 뒷 부분의 Token을 읽어서 decode를 하고 그것을 :(Colon)으로 split 한 다음
     * UsernamePasswordAuthenticationFilter에서 한 것처럼 UsernamePasswordAuthentication Token을 만들고
     * setDetails()로 Request에 대한 Detail 정보를 가지고 온 다음에  UsernamePasswordAuthentication Token을 만들어서 넘김
     * 그런 다음 이전과 같이 똑같이 작업을 한다면
     * BasicAuthenticationFilter에서 doFilterInternal()에서
     * Authentication Manager에게 authRequest를 authenticate로 넘김
     * 그전에 BasicAuthentication Token의 경우에는 authentication이 필요한지 한번 더 체크를 하게 된다
     * 체크를 하는 것은 Security Context Holder을 뒤져봐가지고 만약에 아직 인증받지 않은 상황이면 체크를 하게 되고
     * 만약에 인증을 받은 상황인데 다른 사용자이다라면 그러면 또 인증으로 넘어간다.
     * 그렇지 않은 경우에는 이전에 인증받은 경우 굳이 인증을 다시 받을 필요가 없기 때문에 이 부분은 skip한다.
     * 나머지는 UsernamePasswordAuthentication Token에서 했던 것과 비슷함
     *
     * authentication Token을 Security Context Holder에 넣어준 다음에 remember me 서비스가 있으면 remember me 서비스에서도 success 이벤트를 날려서
     * 쿠키를 셋팅하거나 한다. 그리고 나서 successful authentication 이벤트를 날리는 것이다.
     *
     * 이렇게 해서 사용자가 실제로 Secured한 상황에서 이 메시지를 가지고 오게 된다.
     *
     *
     *
     * */

    //테스트에 사용할 사용자 추가
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                                .build()
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//POST 방식을 테스트 할 때는 CSRF 필터가 작동하기 때문에 disable함
                .authorizeRequests().anyRequest()
                .authenticated() //모든 요청을 막음 -> test시 401 에러로 인증 오류가 발생함
                //.permitAll()//메시지를 가지고 올 수 있도록
                .and()
                .httpBasic()//요청을 httpBasic을 통해서 들어오게 함
                ;

    }
}
