package com.study.project.config;

import com.study.project.auth.filter.JwtAuthenticationFilter;
import com.study.project.auth.filter.JwtAuthorizationFilter;
import com.study.project.auth.handler.CustomAuthenticationSuccessHandler;
import com.study.project.auth.jwt.JwtProperties;
import com.study.project.auth.jwt.service.CookieService;
import com.study.project.auth.jwt.service.JwtTokenizer;
import com.study.project.auth.jwt.utils.JwtAuthorityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenizer jwtTokenizer;

    private final CookieService cookieService;

    private final JwtAuthorityUtils jwtAuthorityUtils;



    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.apply(new CustomFilterConfig());
        http
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/")
                        .deleteCookies(JwtProperties.COOKIE_NAME_ACCESS_TOKEN)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
        ;


        return http.build();
    }

    public class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager =
                    builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter =
                    new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);

            jwtAuthenticationFilter.setAuthenticationSuccessHandler(
                    new CustomAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(
                    new SimpleUrlAuthenticationFailureHandler()
            );

            jwtAuthenticationFilter.setRequiresAuthenticationRequestMatcher(
                    new AntPathRequestMatcher("/user/login","POST"));

            JwtAuthorizationFilter jwtAuthorizationFilter =
                    new JwtAuthorizationFilter(jwtTokenizer, cookieService,jwtAuthorityUtils);

            builder.addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtAuthorizationFilter, JwtAuthenticationFilter.class);

        }
    }
}
