package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final User user;

    //    public CustomOAuth2User(Collection<SimpleGrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, User user) {
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, User user) {
        this.oAuth2User = new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
        this.user = user;
        this.user.setUserName((String) attributes.get("name"));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // SimpleGrantedAuthority に変換する
        return oAuth2User.getAuthorities().stream()
                .map(auth -> (SimpleGrantedAuthority) auth)
                .collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

//    public User getUser() {
//        return user;
//    }
}
