package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.PasswordResetToken;
import com.ims.inventorymgmtsys.entity.User;

import java.time.LocalDateTime;

public interface PasswordResetTokenRepository {

    PasswordResetToken findByToken(String id);
    void save(User user, String token);

    LocalDateTime calculateExpireDate(int expireTimeInHour);

}
