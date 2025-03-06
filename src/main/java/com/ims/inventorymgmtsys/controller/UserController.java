package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.config.CustomUserDetails;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.input.PasswordResetForm;
import com.ims.inventorymgmtsys.service.EmailService;
import com.ims.inventorymgmtsys.service.PasswordResetService;
import com.ims.inventorymgmtsys.service.PasswordResetServiceImpl;
import com.ims.inventorymgmtsys.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final EmailService emailService;

    private final PasswordResetService passwordResetService;

    public  UserController (UserService userService, EmailService emailService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        //OAuth2ユーザか判定
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isOAuth2User = false;
        if (authentication.getPrincipal() instanceof OAuth2User) {
            isOAuth2User = true;
        }
        // 現在のユーザー情報を取得
        User user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("userProfile", user);
            model.addAttribute("isOAuth2User", isOAuth2User);
        } else {
            model.addAttribute("errorMessage", "ユーザを取得できませんでした。");
        }
        return "user/profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute("userProfile") User userProfile, Model model) {
        //OAuth2ユーザか判定
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isOAuth2User = false;
        if (authentication.getPrincipal() instanceof OAuth2User) {
            isOAuth2User = true;
        }
        try {
            userService.updateUserProfile(userProfile);
            model.addAttribute("successUpdateMessage", "更新しました");
        } catch (Exception e) {
            model.addAttribute("errorUpdateMessage", "更新に失敗しました。制約違反の可能性も氏名は変更できません。再度お試しください。");
        }
        // 現在のユーザー情報を取得
        User user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("isOAuth2User", isOAuth2User);

        }
        return "user/profile";
    }

    @GetMapping("/reset")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (passwordResetService.validateResetToken(token)) {
            model.addAttribute("token", token);
            return "user/restPasswordForm";
        } else {
            model.addAttribute("errorMessage", "無効なトークンです");
            return "exception/error";
        }
    }

    @PostMapping("/resetPassword")
    public String resetPassword (Authentication authentication, Model model) {
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        String email = customUserDetails.getEmailAddress();
//        emailService.sendSimpleMessage(email,"リセットリンク送信","リンクをクリックしてパスワードをリセットしてください");
        User user = userService.getCurrentUser();
        if ( user == null ) {
            model.addAttribute("errorMessage", "ユーザを取得できませんでした。");
        }
        String email = user.getEmailAddress();
        if ( email == null || email.isEmpty()) {
            model.addAttribute("errorMessage", "メールアドレスが設定されていません");
        }
        passwordResetService.resetPassword(email);
        model.addAttribute("successMessage", "パスワードリセットリンクが送信されました。" + email);
        model.addAttribute("userProfile", user);
        return "user/profile";
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("passwordReset", new PasswordResetForm());
        return "user/passwordResetForm";
    }




    @PostMapping("/changePassword")
    public String changePassword (@RequestParam("token")String token, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, Model model) {
//        User user = userService.getCurrentUser();
        if ( newPassword == null || !newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "パスワードが異なります");
            return "user/passwordResetForm";
        }

        System.out.println("token of controller is ::::::" + token);
        User user = passwordResetService.validateResetTokenAndGetUser(token);

        if (user == null) {
            model.addAttribute("errorMessage", "無効なトークンです");
            return "user/error";
        }

        if (passwordResetService.validateResetToken(token)) {
            userService.changePassword(user, newPassword);
        }
        model.addAttribute("userProfile", user);
        model.addAttribute("successMessage", "パスワードが変更されました");
        return "user/profile";

    }


}
