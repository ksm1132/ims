package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.PasswordResetToken;
import com.ims.inventorymgmtsys.entity.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcPasswordResetTokenRepository implements PasswordResetTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPasswordResetTokenRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PasswordResetToken findByToken(String token) {
        List<PasswordResetToken> passwordResetTokens = jdbcTemplate.query("SELECT * from t_password_reset_token WHERE token = ?", new BeanPropertyRowMapper<>(PasswordResetToken.class), token);
        System.out.println("passwordResetToken of Repository is :::::::::: " + passwordResetTokens.toString());
        return passwordResetTokens.isEmpty() ? null : passwordResetTokens.get(0);
    }

    @Override
    public void save(User user, String token) {
        jdbcTemplate.update("INSERT INTO t_password_reset_token (token, expireDate, userId) VALUES(?, ?, ?);", token, calculateExpireDate(24), user.getId()
        );
    }

    @Override
    public LocalDateTime calculateExpireDate(int expireTimeInHour) {
        return LocalDateTime.now().plusHours(expireTimeInHour);
    }

}
