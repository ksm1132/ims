package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.exception.UserAlreadyExistsException;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void createUser(User user);

    boolean updateUser(User user);

    User findById(UUID id);

    void registerUser(User user) throws UserAlreadyExistsException;

    User getCurrentUser();

    void updateUserProfile(User userProfile);

    void changePassword(User user, String newPassword);

    Optional<User> findByEmail(String email);

    User findByUserName(String userName);
    int saveMfa(User user);
    User updateMfa(User user);
    boolean updateSecret(User user);
//    User getCurrentUserByDb();
}
