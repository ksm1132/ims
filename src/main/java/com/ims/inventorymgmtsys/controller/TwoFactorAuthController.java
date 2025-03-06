package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.config.*;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.service.TwoFactorService;
import com.ims.inventorymgmtsys.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.snowflake.client.jdbc.internal.org.checkerframework.checker.units.qual.C;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class TwoFactorAuthController {
    private final UserService userService;

    private final TwoFactorAuthenticationCodeVerifier codeVerifier;

    private final QrCode qrCode;

    private final TwoFactorService twoFactorService;

    private final AuthenticationSuccessHandler successHandler;

    private final AuthenticationFailureHandler failureHandler;

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public TwoFactorAuthController(UserService userService, TwoFactorAuthenticationCodeVerifier codeVerifier,
                                   QrCode qrCode, TwoFactorService twoFactorService,AuthenticationSuccessHandler successHandler,
                                   AuthenticationFailureHandler failureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.userService = userService;
        this.codeVerifier = codeVerifier;
        this.qrCode = qrCode;
        this.twoFactorService = twoFactorService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }
    @GetMapping("/mfa-enable-disable")
    public String enableDisableMfa(Model model){
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "auth/mfaEnableDisable";
    }

    @GetMapping("/enable-2fa")
    public String requestEnableMfa(Model model) {
        User user = userService.getCurrentUser();
        if (user.getSecret() == null) {
            String secret = twoFactorService.generateSecretKey();
            user.setSecret(secret);
            userService.updateSecret(user);
        }
        String otpAuthUrl = "otpauth://totp/%s?secret=%s&digits=6".formatted("IMS: " + user.getUserName(), user.getSecret());
        System.out.println("Secret: " + user.getSecret());
        System.out.println("QR Code Data URL: " + this.qrCode.dataUrl(otpAuthUrl));
        model.addAttribute("qrCode", this.qrCode.dataUrl(otpAuthUrl));
        model.addAttribute("secret", user.getSecret());
        return "auth/enable-2fa";
    }

    @PostMapping("/enable-2fa")
    public String processEnableMfa(@RequestParam String code, @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model, HttpServletRequest request) {
        User user = userService.getCurrentUser();
        if (user.getMfaEnabled()) {
            return "redirect:/";
        }
        if (!this.codeVerifier.verify(user, code)) {
            model.addAttribute("message", "Invalid code");
            return this.requestEnableMfa(model);
        }
        System.out.println("Received code::::::::::::: " + code);
        System.out.println("Current user::::::::::::::: " + user);

        User enabled = userService.updateMfa(user);
        User updatedUser = userService.getCurrentUser();
        Authentication token = UsernamePasswordAuthenticationToken.authenticated(new CustomUserDetails(updatedUser), null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        request.getSession().setAttribute("twoFactorAuthentication", token);
        model.addAttribute("user", updatedUser);

        System.out.println("MFA Enabled: " + updatedUser.getMfaEnabled());
        return "auth/mfaEnableDisable";
    }

    @GetMapping("/challenge/totp")
    public String requestTotp() {
        return "auth/totp";
    }

@PostMapping("/challenge/totp")
public String processTotp(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Authentication currentAuth = (Authentication) request.getSession().getAttribute("twoFactorAuthentication");
    CustomUserDetails customUserDetails = (CustomUserDetails) currentAuth.getPrincipal();
    User user = customUserDetails.getUser();
        // TOTP コードの検証処理
    if (user != null && codeVerifier.verify(user, code)) {
        // 認証成功時の処理
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, currentAuth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Authenticated user: " + authentication.getName());
        request.getSession().removeAttribute("twoFactorAuthentication");
        customAuthenticationSuccessHandler.handleSuccessRedirect(request, response, authentication);
        return null;
//        return "redirect:/catalog/list";
    } else {
        // 認証失敗時の処理
        return "redirect:/challenge/totp?error=true";
    }
}


}
