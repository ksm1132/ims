package com.ims.inventorymgmtsys.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class PasswordResetToken {
    private String id;
    private String token;
    private LocalDateTime expireDate;
    private UUID userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String token() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
