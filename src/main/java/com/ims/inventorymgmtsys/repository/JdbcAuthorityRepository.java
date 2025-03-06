package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Authorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcAuthorityRepository implements AuthorityRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcAuthorityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Authorities> getRole(String username) {
        return jdbcTemplate.query("SELECT authority FROM authorities WHERE username = ?", new BeanPropertyRowMapper<>(Authorities.class), username);
    }

    @Override
    public void saveAuthority(Authorities authority) {
        jdbcTemplate.update("INSERT INTO authorities (username, authority) VALUES (?, ?)", authority.getUsername(), authority.getAuthority());
    }
}
