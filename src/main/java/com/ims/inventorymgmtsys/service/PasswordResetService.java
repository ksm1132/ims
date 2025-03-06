package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.PasswordResetToken;
import com.ims.inventorymgmtsys.entity.User;

import java.util.Optional;

public interface PasswordResetService {
    void createPasswordResetTokenForUser(User user,String token);
    void resetPassword(String email);
    boolean validateResetToken(String token);
    User validateResetTokenAndGetUser(String token);
    boolean updatePassword(String token, String newPassword);
    void createToken(String email);
    boolean isOAuth2User(String email);

}
