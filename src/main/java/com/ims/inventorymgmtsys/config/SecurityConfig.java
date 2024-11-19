package com.ims.inventorymgmtsys.config;

import com.ims.inventorymgmtsys.service.*;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private LoginUserDetailService loginUserDetailService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    private final PasswordEncoder passwordEncoder;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;



    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    private final AuditlogService auditlogService;
    private final UserService userService;

    @Autowired
    public SecurityConfig(AuditlogService auditlogService, UserService userService, PasswordEncoder passwordEncoder, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.auditlogService = auditlogService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new ForwardedHeaderFilter(), WebAsyncManagerIntegrationFilter.class)
                .cors(Customizer.withDefaults())
//                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests()
                    .requestMatchers("/fragments/**","/js/**","/css/**","/images/**","/favicon.ico").permitAll()
                    .requestMatchers("/login", "/register","/forget","/password","/changePasswordNoLogin").permitAll()
                    .requestMatchers("/catalog/**", "/order/**", "/cart/**","/user/**","/challenge/**","/enable-2fa/**","/api/products/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER","OIDC_USER")
                    .requestMatchers("/admin/**","/sales/**","/system/**","/api/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/challenge/totp").access(new TwoFactorAuthorizationManager())
                    .requestMatchers("/h2-console/**").permitAll()  // H2コンソールへのアクセスを許可
                    .requestMatchers("/error").permitAll() // /error へのアクセスを認証済みユーザーに制限
                    .anyRequest().authenticated()
                .and()
                .securityContext(securityContext -> securityContext.requireExplicitSave(false))
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
//                    .userDetailsService(loginUserDetailService)
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .defaultSuccessUrl("/catalog/list", true)
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                .and()
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                .and()
                .csrf()
                    .ignoringRequestMatchers("/api/**")                // その他のリクエストは全て許可
                .and()
                    .httpBasic()
                .and()
                .exceptionHandling()
                    .accessDeniedPage("/access-denied")
                .and()
                .headers()
                    .xssProtection()
                    .and()
                    .frameOptions().sameOrigin()  // フレーム内でH2コンソールを表示できるように設定
                .and();
//                .requestCache().disable();

        http
                .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                                .and()
                                        .rememberMe()
                                                .rememberMeCookieDomain("IMSApp")
                                                        .useSecureCookie(true)
                                                                .tokenValiditySeconds(86400);

        http.userDetailsService(loginUserDetailService);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public TwoFactorAuthenticationCodeVerifier mfaFactorAuthenticationCodeVerifier() {
        return new TotpAuthenticationCodeVerifier();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginUserDetailService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        String myappDomain = System.getenv("myappDomain");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://" + myappDomain, "http://" + myappDomain + ":8080", "null","localhost"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
        ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
        FilterRegistrationBean<ForwardedHeaderFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setUrlPatterns(List.of("/*"));
        return registration;
    }



}
