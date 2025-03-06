package com.ims.inventorymgmtsys.config;

import com.ims.inventorymgmtsys.entity.Auditlog;
import com.ims.inventorymgmtsys.service.AuditlogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    private final AuditlogService auditlogService;

    public CustomAuthenticationFailureHandler(AuditlogService auditlogService) {
        this.auditlogService = auditlogService;
    }
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        logger.info("Authentication failure for user {}", username);

        try {
            auditlogService.save(createAuditlog(username, "LOGIN_FAILURE", "Login attempt failed:" + exception.getMessage()));
            logger.info("auditlog successfully");
        } catch (Exception e) {
            logger.error("Error saving for db {}", e.getMessage());
        }
        response.sendRedirect("/login?failure=true");
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
