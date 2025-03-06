package com.ims.inventorymgmtsys.config;

import com.ims.inventorymgmtsys.entity.User;

public interface TwoFactorAuthenticationCodeVerifier {
    boolean verify(User user, String code);
}
