package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.PasswordResetToken;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private final EmailService emailService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;

    public PasswordResetServiceImpl(EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository, UserService userService) {
        this.emailService =emailService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userService = userService;
    }

    @Override
    public void createToken(String email) {
        String resetToken = UUID.randomUUID().toString();
        String myappDomain = System.getenv("myappDomain");
//        String myappDomain = "localhost:8080";
        String resetUrl = "http://" + myappDomain + "/changePasswordNoLogin?token=" + resetToken;
        Optional<User> users = userService.findByEmail(email);
        if (users.isPresent()){
            User user = users.get();
            System.out.println("Optional user of Repository is ::::::::" + users);
            System.out.println("user of Repository is ::::::::" + user);
            passwordResetTokenRepository.save(user, resetToken);
        }
        emailService.sendSimpleMessage(email, "reset Password", "以下のリンクをクリックしてパスワードをリセットしてください。\n" + resetUrl);

    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setExpireDate(passwordResetTokenRepository.calculateExpireDate(24));
        passwordResetToken.setUserId(user.getId());
    }

    @Override
    public boolean updatePassword(String token, String newPassword) {
        User user = userService.getCurrentUser();
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken != null && user.getId().equals(passwordResetToken.getUserId())) {
            user.setPassword(newPassword);
            return userService.updateUser(user);
        }
        return false;
    }

    @Override
    public void resetPassword(String email) {
        User user = userService.getCurrentUser();
        String resetToken = UUID.randomUUID().toString();
        String myappDomain = System.getenv("myappDomain");
        if ( myappDomain == null || myappDomain.isEmpty()){
            throw new IllegalStateException("環境変数に間違いがあります");
        }
        String resetUrl = "https://" + myappDomain + "/user/changePassword?token=" + resetToken;
        passwordResetTokenRepository.save(user, resetToken);

        emailService.sendSimpleMessage(email, "reset Password", "以下のリンクをクリックしてパスワードをリセットしてください。\n" + resetUrl);

    }

    @Override
    public boolean validateResetToken(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);

        if ( token == null ) {
            return false;
        }

        return !resetToken.getExpireDate().isBefore(LocalDateTime.now());
    }

    @Override
    public User validateResetTokenAndGetUser(String token) {
        System.out.println("token of service is ::::::" + token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        System.out.println("paswordResetToken of service id :::::" + passwordResetToken.toString());
        System.out.println("paswordResetToken of service expiry_date :::::" + passwordResetToken.getExpireDate());
        System.out.println("paswordResetToken of service current date and time is :::::" + LocalDateTime.now());
        if (passwordResetToken == null || passwordResetToken.getExpireDate() == null || passwordResetToken.getExpireDate().isBefore(LocalDateTime.now())){
            return null;
        }
        System.out.println("getuser.getId is :::::::::::" + passwordResetToken.getUserId());
        return userService.findById(passwordResetToken.getUserId());
    }

    @Override
    public boolean isOAuth2User(String email) {
        User user = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("ユーザが存在しません"));
        return user.getPassword() == null || user.getPassword().isEmpty();
    }
}
