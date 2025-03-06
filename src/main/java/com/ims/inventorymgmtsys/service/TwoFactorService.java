package com.ims.inventorymgmtsys.service;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.stereotype.Service;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import java.security.GeneralSecurityException;

@Service
public class TwoFactorService {
    public boolean check(String hexKey, String code) {
        try {
            return TimeBasedOneTimePasswordUtil.validateCurrentNumberHex(hexKey, Integer.parseInt(code), 10000);
        }
        catch (GeneralSecurityException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public String generateSecretKey() {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }
}
