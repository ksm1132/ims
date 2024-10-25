package com.ims.inventorymgmtsys.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.snowflake.client.jdbc.internal.google.api.Http;
import org.springframework.web.servlet.HandlerInterceptor;

public class CustomHeaderInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        if (forwardedProto == null) {
            // Default to "http" if no "X-Forwarded-Proto" header is set
            forwardedProto = request.isSecure() ? "https" : "http";
        }
        // Set custom header with the determined protocol
        response.setHeader("X-Custom-Header", forwardedProto);
        return true;
    }
}
