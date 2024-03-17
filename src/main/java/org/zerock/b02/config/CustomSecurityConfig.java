package org.zerock.b02.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b02.security.CustomUserDetailService;
import org.zerock.b02.security.handler.Custom403Handler;

import javax.sql.DataSource;

@Configuration
@Slf4j
//@EnableGlobalMethodSecurity(prePostEnabled = true) // Deprecated
@EnableMethodSecurity(prePostEnabled = true)    //  prePostEnabled 속성이 있으면 원하는 곳에 @PreAuthorize 또는 @PostAuthorize 를 이용해 권한을 체크할 수 있음
@RequiredArgsConstructor
public class CustomSecurityConfig {

//    주입이 필요함 -> 생성자를 통해 주입: lombok 의 RequiredArgConstructor 이용해서 주입 받음
    private final DataSource dataSource;
    private final CustomUserDetailService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("-----------security configure------------");

//        httpSecurity.formLogin().loginPage("/member/login");  //  Deprecated
//        httpSecurity.csrf().disable();                        //  Deprecated
//        httpSecurity.rememberMe();                            //  Deprecated
        httpSecurity
                .formLogin(formLogin -> formLogin
                        .loginPage("/member/login"))   //  커스텀 로그인 페이지
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(rememberMe -> rememberMe    //  자동 로그인 처리
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(userDetailService)
                        .tokenValiditySeconds(60 * 60 * 24 * 30));  //  쿠키 유효 시간 30일

        return httpSecurity.build();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }



    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
//    정적 리소스는 필터를 거치지 않도록 처리
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("---------web security configure----------");

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }



}
