package com.ims.inventorymgmtsys.config;

import com.ims.inventorymgmtsys.service.UserService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

public class TwoFactorAuthentication extends AbstractAuthenticationToken {
    private final Authentication first;
    private final UserService userService;
    private boolean authenticated;
//    private boolean mfaCompleted;

    public TwoFactorAuthentication(Authentication first, UserService userService) {
        super(first.getAuthorities());
        this.first = first;
        this.userService = userService;
        this.authenticated = false;
    }

    @Override
    public Object getPrincipal() {
        return this.first.getPrincipal();
    }

    @Override
    public Object getCredentials() {
        return first.getCredentials();
    }

    @Override
    public void eraseCredentials() {
        if (this.first instanceof CredentialsContainer) {
            ((CredentialsContainer) this.first).eraseCredentials();
        }
    }

//    @Override
//    public boolean isAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return isMfaRequired(authentication) && super.isAuthenticated();
//    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            super.setAuthenticated(true);
        } else {
            super.setAuthenticated(false);
        }
        this.authenticated = authenticated;
    }


    public Authentication getFirst() {
        return this.first;
    }

    public UserService getUserService() {
        return userService;
    }

//    private boolean isMfaRequired() {
//        User user = userService.getCurrentUser();
//        return user.getMfaEnabled();
//    }

//    private boolean isMfaRequired(Authentication authentication) {
//        if (authentication == null) {
//            return false;
//        }
//        Object principal = authentication.getPrincipal();
//        if (!(principal instanceof CustomUserDetails)) {
//            return false;
//        }
//        CustomUserDetails customUserDetails = (CustomUserDetails) principal;
//        User user = customUserDetails.getPrincipal();
//        if (user == null) {
//            return false;
//        }
//        return user.getMfaEnabled();
//    }

//    private boolean isMfaRequired(Authentication authentication) {
//        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
//            return false;
//        }
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        User user = userDetails.getUser(); // getUser() メソッドで User を取得
//        if (user == null) {
//            return false; // User が null の場合は MFA は不要
//        }
//        return user.getMfaEnabled(); // MFA が有効かどうかをチェック
//    }

//    public boolean isMfaCompleted() {
//        return mfaCompleted;
//    }
//
//    public void setMfaCompleted(boolean mfaCompleted) {
//        this.mfaCompleted = mfaCompleted;
//    }

}
