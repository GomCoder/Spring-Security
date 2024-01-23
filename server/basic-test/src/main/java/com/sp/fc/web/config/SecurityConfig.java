package com.sp.fc.web.config;

//일반적인 User가 로그인 후 localhost:9050/user와 localhost:9050/admin에 접근하여 정보를 탈취하여 악용하는 것을 막기 위한 설정
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) //PrePost로 권한 체크를 하겠다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //application.yml에서는 사용자를 1명만 추가할 수 있음
    //사용자를 추가하고자 한다면...WebSecurityConfigurerAdapter에서 간단하게 추가할 수 있음
    //이것을 설정하게 되면 application.yml에 있는 것은 더이상 듣지 않게 됨
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.builder() //일반 사용자 추가
                        .username("user2")
                        .password(passwordEncoder().encode("2222"))
                        .roles("USER"))
                .withUser(User.builder() //Admin 추가
                        .username("admin")
                        .password(passwordEncoder().encode("3333"))
                        .roles("ADMIN"))
                ;
    }

    //Password Encoder를 Bean으로 만든다
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //BCrypt 인코더 사용
    }

    //Security에서는 기본적으로 모든 페이지의 접근을 막고 시작함
    //일반적으로 다 공개를 하고자 한다면...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) ->
                requests.antMatchers("/").permitAll() //홈페이지 같은 경우 모든 사람에게 접근을 허용
                        .anyRequest().authenticated()
                ); //authorize할 때 Request에서 어떤 request든 다 authenticated(인증) 받은 상태에서 접근해라
        http.formLogin();
        http.httpBasic();
    }
}
