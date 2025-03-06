package com.ims.inventorymgmtsys.utils;

import com.ims.inventorymgmtsys.config.CustomUserDetails;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.repository.UserRepository;
import com.ims.inventorymgmtsys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {
    private final UserRepository userRepository;

    @Autowired
    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UUID getCurrentId () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication != null ) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                return userDetails.getUserId();
            } else if (principal instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) principal;
                String email = (String) oAuth2User.getAttribute("email");
                UUID id = userRepository.findByEmail(email).map(User::getId).orElse(null);
                System.out.println("OAuth2User Attributes:::::::::::::::::::::::: " + oAuth2User.getAttributes());
                return id;
            }
        }
        return null;
    }

}
