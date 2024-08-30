package com.ims.inventorymgmtsys.config;

import com.ims.inventorymgmtsys.entity.Auditlog;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.repository.AuthorityRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    private final AuthorityRepository authorityRepository;


    private final UserService userService;
//    private final String secondUrl;


    private final AuditlogService auditlogService;


    @Autowired
    public CustomAuthenticationSuccessHandler(AuditlogService auditlogService, UserService userService, AuthorityRepository authorityRepository) {
        this.auditlogService = auditlogService;
        this.authorityRepository = authorityRepository;
        this.userService = userService;
    }


@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    try {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Authenticated user roles: " + authentication.getAuthorities());
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = customUserDetails.getUser();
        if (user.getMfaEnabled()) {
            logger.info("MFA is required. Redirecting to MFA page");
            request.getSession().setAttribute("twoFactorAuthentication", auth);
            Authentication newAuth = new TwoFactorAuthentication(auth, userService);
            newAuth.setAuthenticated(true);
            System.out.println("Authentication status: " + newAuth.isAuthenticated());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
            response.sendRedirect("/challenge/totp");
            auditlogService.save(createAuditlog(authentication.getName(), "LOGIN_SUCCESS", "User logged in Successfully"));
            return;

        }
        handleSuccessRedirect(request, response, authentication);
    } catch (Exception e) {
        logger.error("Authentication success handler error: ", e);
        response.sendRedirect("/error"); // エラーページへのリダイレクト
    }
}

private void handleSuccessRedirect(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

    String targetUrl = isAdmin ? "/admin/management" : "/catalog/list";
    logger.info("Authentication successful. Redirecting to: " + targetUrl);
    auditlogService.save(createAuditlog(authentication.getName(), "LOGIN_SUCCESS", "User logged in Successfully"));
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
