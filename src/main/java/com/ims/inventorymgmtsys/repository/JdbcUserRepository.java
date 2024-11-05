package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.config.CustomUserDetails;
import com.ims.inventorymgmtsys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class JdbcUserRepository implements UserRepository{

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, User> emailToCustomUser;


    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, Map<String, User> emailToCustomUser) {
        this.jdbcTemplate = jdbcTemplate;
        this.emailToCustomUser = emailToCustomUser;
    }


    @Override
    public User findById(UUID id) {
        try {
            List<User> users = jdbcTemplate.query(
                    "SELECT * FROM t_user WHERE id = ?",
                    new BeanPropertyRowMapper<>(User.class),
                    id
            );
            return users.isEmpty() ? null : users.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM t_user", new DataClassRowMapper<>(User.class));
    }

    @Override
    public void save(User user){
        try {
        UUID id = UUID.randomUUID();
        user.setId(id);
        jdbcTemplate.update("INSERT into t_user VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                user.getId(),
                user.getUserName(),
                user.getEmailAddress(),
                user.getAddress(),
                user.getPhone(),
                user.getPassword(),
                user.getEnabled(),
                user.getSecret(),
                user.getMfaEnabled()
                );
        } catch (DuplicateKeyException e) {
            throw new DataIntegrityViolationException("ユーザがすでに存在します : " + user.getUserName(), e);
        }
    }

    @Transactional
    @Override
    public boolean update(User user) {
        System.out.println("Updating user with ID:::::: " + user.getId());
        jdbcTemplate.queryForObject(
                "SELECT * FROM t_user WHERE id = ? FOR UPDATE",
                new Object[]{user.getId()},
                (rs, rowNum) -> user.getId()  // 必要な情報を返すために、RowMapperを簡単に指定
        );

        int count = jdbcTemplate.update("UPDATE t_user SET userName=?, emailAddress=?, address=?, phone=? WHERE id=?",
                user.getUserName(),
                user.getEmailAddress(),
                user.getAddress(),
                user.getPhone(),
                user.getId()
                );
        return count != 0;
    }

    @Override
    public User findByUserName(String userName) {
        List<User> users = jdbcTemplate.query("SELECT * FROM t_user WHERE userName = ?", new BeanPropertyRowMapper<>(User.class), userName);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM t_user WHERE emailAddress = ?", new BeanPropertyRowMapper<>(User.class),email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public boolean updateAuth(User user) {
        int count = jdbcTemplate.update("INSERT INTO authorities (username, authority) VALUES (?, ?)", user.getUserName(), "ROLE_USER");
        return count != 0;
    }

    @Override
    public boolean updateIsEnabled(User user) {
        int count = jdbcTemplate.update("UPDATE t_user set enabled=TRUE WHERE username=?", user.getUserName());
        return count != 0;
    }

    @Override
    public boolean updatePassword(User user) {
        System.out.println("Updating user with ID: " + user.getId());
        int count = jdbcTemplate.update("UPDATE t_user SET password=? WHERE id=?",
                user.getPassword(),
                user.getId()
        );
        return count != 0;
    }

//    @Override
//    public User findCustomUserByEmail(String email) {
//        return this.emailToCustomUser.get(email);
//    }

    @Override
    public int saveMfa(User user){
        return jdbcTemplate.update("INSERT INTO t_user (userName, password, secret, mfaEnabled) VALUES (?, ?, ?, ?)",
                user.getUserName(),
                user.getPassword(),
                user.getSecret(),
                user.getMfaEnabled());

    }

    @Override
    public User updateMfa(User user) {
        jdbcTemplate.update("UPDATE t_user SET mfaEnabled = true WHERE userName = ?", user.getUserName());
//        user.setMfaEnabled(true);
        return user;
    }

    @Override
    public boolean updateSecret(User user) {
        int count = jdbcTemplate.update("UPDATE t_user SET secret = ? WHERE userName = ?", user.getSecret(), user.getUserName());

        return count != 0;
    }

}
