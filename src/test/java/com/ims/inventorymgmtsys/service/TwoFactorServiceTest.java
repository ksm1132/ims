package com.ims.inventorymgmtsys.service;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

public class TwoFactorServiceTest {

    @Test
    void test_checkKey() throws GeneralSecurityException {
        TwoFactorService twoFactorService = new TwoFactorService();

        String secretKey = twoFactorService.generateSecretKey();
        assertNotNull(secretKey, "Generated secret key should not be null.");
        assertFalse(secretKey.isEmpty(), "Generated secret key should not be empty.");

    }

    @Test
    void test_checkCode() throws GeneralSecurityException {
        TwoFactorService twoFactorService = new TwoFactorService();
        String secretKey = "A3F6B8E9";
        String validCode = String.valueOf(TimeBasedOneTimePasswordUtil.generateCurrentNumberHex(secretKey));
        assertTrue(twoFactorService.check(secretKey, String.valueOf(validCode)), "The valid code");
    }

}
