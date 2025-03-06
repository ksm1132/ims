package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Auditlog;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcAuditlogRepository implements AuditlogRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAuditlogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Auditlog auditlog) {
        jdbcTemplate.update("INSERT INTO audit_log (username, eventType, details) VALUES (?, ?, ?)",
        auditlog.getUsername(), auditlog.getEventType(), auditlog.getDetails());
    }

    @Override
    public List<Auditlog> findAll() {
        return jdbcTemplate.query("SELECT * FROM audit_log", new BeanPropertyRowMapper<>(Auditlog.class));
    }
}
