package com.ims.inventorymgmtsys.config;

import com.ims.inventorymgmtsys.entity.User;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.springframework.util.StringUtils;

import java.security.GeneralSecurityException;

public class TotpAuthenticationCodeVerifier implements TwoFactorAuthenticationCodeVerifier {
    @Override
    public boolean verify(User user, String code) {

        if (user == null || !StringUtils.hasText(user.getSecret())) {
            throw new IllegalArgumentException("User's secret is missing or Invalid");
        }

        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        try {
            int optCode = Integer.parseInt(code);
            return TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecret(), optCode, 10000);
//            return TimeBasedOneTimePasswordUtil.validateCurrentNumber(user.getSecret(), StringUtils.hasText(code) ? Integer.parseInt(code) : 0, 10000);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Code must be a valid integer", e);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Security exception during OTP validation", e);
        }
    }
}
