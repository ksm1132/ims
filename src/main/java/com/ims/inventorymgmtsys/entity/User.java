package com.ims.inventorymgmtsys.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private UUID id; //PK
    private String userName;
    private String emailAddress;
    private String address;
    private String phone;
    private String password;
    private boolean enabled;

    // New fields for two-factor authentication
    private String secret;
    private boolean mfaEnabled;


    public UUID getId() {
        return id;
    }


    public void setId(UUID id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String customerName) {
        this.userName = customerName;
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAdress) {
        this.emailAddress = emailAdress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public boolean getMfaEnabled() {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled) {
        this.mfaEnabled = mfaEnabled;
    }



}
