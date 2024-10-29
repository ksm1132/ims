package com.ims.inventorymgmtsys.config;

import com.ims.inventorymgmtsys.entity.Auditlog;
import com.ims.inventorymgmtsys.entity.Authorities;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.repository.AuthorityRepository;
import com.ims.inventorymgmtsys.repository.UserRepository;
import com.ims.inventorymgmtsys.service.AuditlogService;
import com.ims.inventorymgmtsys.service.UserService;
import jakarta.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private final AuthorityRepository authorityRepository;


    private final UserService userService;
    private final UserRepository userRepository;
//    private final String secondUrl;


    private final AuditlogService auditlogService;


    @Autowired
    public CustomAuthenticationSuccessHandler(AuditlogService auditlogService, UserService userService, AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.auditlogService = auditlogService;
        this.authorityRepository = authorityRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }


@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authenticated user roles: " + authentication.getAuthorities());

        //OAuth2
//        boolean isOAuth2 = authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_OAUTH2"));
        boolean isOAuth2 = authentication instanceof OAuth2AuthenticationToken;

        if (isOAuth2) {
            handleSuccessRedirect(request, response, authentication);
            return;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        if (user.getMfaEnabled()) {
            logger.info("MFA is required. Redirecting to MFA page");
            request.getSession().setAttribute("twoFactorAuthentication", auth);
            Authentication newAuth = new TwoFactorAuthentication(auth, userService);
            newAuth.setAuthenticated(true);
            System.out.println("Authentication status: " + newAuth.isAuthenticated());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            if (!response.isCommitted()) {
                response.sendRedirect("/challenge/totp");
            }
            auditlogService.save(createAuditlog(authentication.getName(), "LOGIN_SUCCESS", "User logged in Successfully"));
            return;

        }
        handleSuccessRedirect(request, response, authentication);
    } catch (Exception e) {
        logger.error("Authentication success handler error: ", e);
        response.sendRedirect("/error"); // エラーページへのリダイレクト
    }
}

public void handleSuccessRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    authentication.getAuthorities().forEach(authority -> logger.info("Authority::::" + authority.getAuthority()));
    String firstAuthority = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse(null);
    logger.info("First::::::::::::" + firstAuthority);
        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

    String targetUrl = isAdmin ? "/admin/management" : "/catalog/list";

    String username;
    String email = "";
    String fullName = "";

    if (authentication instanceof OAuth2AuthenticationToken) {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = oauthToken.getPrincipal();

        // 属性を取得
        fullName = oauthUser.getAttribute("name"); // ユーザーのフルネーム
        email = oauthUser.getAttribute("email"); // ユーザーのメールアドレス
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (!existingUser.isPresent()) {
            User user = new User();
            user.setUserName(fullName);
            user.setEmailAddress(email);
            user.setEnabled(true);
            userRepository.save(user);

            Authorities authority = new Authorities();
            authority.setUsername(fullName);
            authority.setAuthority(firstAuthority);
            authorityRepository.saveAuthority(authority);
        } else {
            logger.info("User already exists :::::" + email);
        }
    } else {
        // Basic 認証や他の認証方式の場合
        username = authentication.getName();
        fullName = username; // ここでは username をそのまま使用
    }
    logger.info("new user created:" + email + " name is :" + fullName);
    logger.info("Authentication successful. Redirecting to: " + targetUrl);

    auditlogService.save(createAuditlog(fullName, "LOGIN_SUCCESS", "User logged in Successfully"));
    response.sendRedirect(targetUrl);
}


    private Auditlog createAuditlog(String username, String eventType, String details) {
        Auditlog auditlog = new Auditlog();
        auditlog.setUsername(username);
        auditlog.setEventType(eventType);
        auditlog.setDetails(details);
        auditlog.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return auditlog;
    }

}
