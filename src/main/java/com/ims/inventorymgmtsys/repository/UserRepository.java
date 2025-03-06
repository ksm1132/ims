package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User findById(UUID id);

    List<User> findAll();

    void save(User user);

    boolean update(User user);

    User findByUserName(String userName);

    Optional<User> findByEmail(String email);  // 新しいメソッドの追加

    boolean updateAuth(User user);

    boolean updateIsEnabled(User user);

    boolean updatePassword(User user);

    // New fields for two-factor authentication

    int saveMfa(User user);
    User updateMfa(User user);
    boolean updateSecret(User user);
}
