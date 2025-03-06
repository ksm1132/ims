package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.exception.UserAlreadyExistsException;
import com.ims.inventorymgmtsys.repository.UserRepository;
import com.ims.inventorymgmtsys.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final SecurityUtils securityUtils;


    public UserServiceImpl(UserRepository userRepository, JdbcTemplate jdbcTemplate, SecurityUtils securityUtils, PasswordEncoder passwordEncoder){

        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.securityUtils = securityUtils;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void createUser(User user) {
        System.out.println("received data :" + user);
        User createuser = new User();
        createuser.setId(UUID.randomUUID());
        createuser.setUserName(user.getUserName());
        createuser.setEmailAddress(user.getEmailAddress());
        createuser.setAddress(null);
        createuser.setPhone(null);
        createuser.setPassword(passwordEncoder.encode(user.getPassword())); // 修正
        userRepository.save(createuser);
        userRepository.updateAuth(createuser);
        userRepository.updateIsEnabled(createuser);

    }

    @Override
    public boolean updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public void registerUser(User user) throws UserAlreadyExistsException {
//        User existsUser = selectById(user.getId());
        User existsUserByUserName = userRepository.findByUserName(user.getUserName());

        Optional<User> existsUserByEmail = userRepository.findByEmail(user.getEmailAddress());
        if ( existsUserByUserName != null || existsUserByEmail.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
            createUser(user);
    }


    @Override
    public User getCurrentUser() {
        UUID currentUserId = securityUtils.getCurrentId();
        if (currentUserId != null) {
            return userRepository.findById(currentUserId);
        }
        return null;
    }

//    @Override
//    public User getCurrentUserByDb() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null || authentication.getPrincipal() instanceof User) {
//            return (User) authentication.getPrincipal();
//        }
//        return null;
//    }

    @Override
    public void updateUserProfile(User userProfile) {
        User user = getCurrentUser();
        if (user != null) {
            user.setId(userProfile.getId());
            user.setUserName(userProfile.getUserName());
            user.setEmailAddress(userProfile.getEmailAddress());
            user.setAddress(userProfile.getAddress());
            user.setPhone(userProfile.getPhone());
            updateUser(user);
        }
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.updatePassword(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public int saveMfa(User user) {
        return userRepository.saveMfa(user);
    }

    @Override
    public User updateMfa(User user) {
        return userRepository.updateMfa(user);
    }

    @Override
    public boolean updateSecret(User user) {
        return userRepository.updateSecret(user);
    }


}
