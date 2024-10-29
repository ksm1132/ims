package com.ims.inventorymgmtsys.service;


import com.ims.inventorymgmtsys.entity.Authorities;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.repository.AuthorityRepository;
import com.ims.inventorymgmtsys.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

    private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        logger.info("loadUser method called with OAuth2UserRequest: " + oAuth2UserRequest);

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String email = oAuth2User.getAttribute("email");
            String principalId = oAuth2User.getAttribute("name");
            String givenName = oAuth2User.getAttribute("given_name");
            String familyName = oAuth2User.getAttribute("family_name");
//        String name = givenName + " " + familyName;
            String name = (String) attributes.get("name");

            Optional<User> userOptional = userRepository.findByEmail(email);


            User user;
            if (userOptional.isPresent()) {
                user = userOptional.get();
            } else {
                user = new User();
                user.setUserName(name);
                user.setEmailAddress(email);
                user.setEnabled(true);
                userRepository.save(user);
                logger.info("new user created:" + email + " name is :" + name);

                Authorities authority = new Authorities();
                authority.setUsername(name);
                authority.setAuthority("OIDC_USER");
                authorityRepository.saveAuthority(authority);

            }

            List<Authorities> authoritiesList = authorityRepository.getRole(user.getUserName());
            List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Authorities authority : authoritiesList) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
            }
//        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authorities.get(0).getAuthority());

//        CustomOAuth2User customOAuth2User = new CustomOAuth2User(
//                Collections.singletonList(grantedAuthority),oAuth2User.getAttributes(),"name"
//        );
            CustomOAuth2User customOAuth2User = new CustomOAuth2User(
                    grantedAuthorities, oAuth2User.getAttributes(), "name", user
            );
            return customOAuth2User;

    }

}
