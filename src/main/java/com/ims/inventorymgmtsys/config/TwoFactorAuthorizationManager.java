package com.ims.inventorymgmtsys.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.core.Authentication;

import java.util.function.Supplier;

public class TwoFactorAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {

        Authentication auth = authentication.get();
        if (auth instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
            if (token.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) token.getPrincipal();
                return new AuthorizationDecision(true);
            }
        }
        return new AuthorizationDecision(false);

//        return new AuthorizationDecision(authentication.get() instanceof TwoFactorAuthentication);
//        Authentication auth = authentication.get();
//        System.out.println("Authentication: " + auth);
//        // MFA が完了しているかどうかを確認するためのカスタムロジック
//        if (auth instanceof TwoFactorAuthentication) {
//            TwoFactorAuthentication mfaAuth = (TwoFactorAuthentication) auth;
//            System.out.println("TwoFactorAuthentication: " + mfaAuth);
//            // ここで MFA 完了の確認を行います
//            // 例: MFA 認証が完了している場合に特定の権限を付与する
//            if (mfaAuth.isMfaCompleted()) {
//                return new AuthorizationDecision(true);
//            }
//        }
//        return new AuthorizationDecision(false);
    }
}