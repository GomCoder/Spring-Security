package com.sp.fc.web.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

   private final CustomAuthDetails customAuthDetails;

    public SecurityConfig(CustomAuthDetails customAuthDetails) {
        this.customAuthDetails = customAuthDetails;
    }

    //로그인 처리

    /**
     * 로그인 처리를 위한 사용자 등록(일반 사용자: user1, 관리자: admin)
     * @param auth the {@link AuthenticationManagerBuilder} to use
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()//Test에 한정하여 사용하는 메서드
                                .username("user1")
                                .password("1111")
                                .roles("USER")
                ).withUser(
                        User.withDefaultPasswordEncoder()
                                .username("admin")
                                .password("2222")
                                .roles("ADMIN")
                );
    }

    /**
     * 로그인한 관리자가 유저 페이지에도 접근이 되게 하려면
     * @return
     */
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN >  ROLE_USER"); //admin은 user가 할 수 있는 것은 다 할 수 있다 설정
        return roleHierarchy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .authorizeRequests(request->{
                    //1. 메인페이지를 제외하고는 모두 막아보기
                    request
                            .antMatchers("/").permitAll() //메인 페이지에 모두 접근 허용
                            .anyRequest().authenticated() //그 외에는 접근시 인증 필요
                            ;
                })
                //.formLogin() //UsernamePassword 설정: 로그인 페이지를 설정 안하면 DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter를 타게 됨
                .formLogin(
                        login -> login.loginPage("/login")
                                .permitAll()
                                .defaultSuccessUrl("/", false) //로그인 후 메인페이지로
                                .failureUrl("/login-error")//로그인 에러 페이지
                                .authenticationDetailsSource(customAuthDetails)
                )
                .logout(logout -> logout.logoutSuccessUrl("/")) //로그 아웃 후에도 메인 페이지로
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
                ;
    }

    //웹 리소스에 대해서 Security Filter가 적용되지 않도록 ignore 시킴
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                )
                ;
    }
}
