package com.ims.inventorymgmtsys.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = (Authentication) request.getUserPrincipal();
        String userName = null;

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            userName = oauthToken.getPrincipal().getAttribute("name");
        } else if (authentication != null) {
            userName = authentication.getName();
        }

        request.setAttribute("userName", userName); // リクエストにユーザー名を設定
        return true; // 次の処理に進む
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            String userName = (String) request.getAttribute("userName");
            modelAndView.addObject("userName", userName); // モデルにユーザー名を追加
        }
    }
}

