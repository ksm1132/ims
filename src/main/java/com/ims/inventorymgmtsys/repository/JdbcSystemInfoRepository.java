package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.SystemInfo;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcSystemInfoRepository implements SystemInfoRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcSystemInfoRepository( JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(SystemInfo systemInfo) {
        String sql = "INSERT INTO system_info (availableProcessors, systemLoadAverage, usedHeapMemory, maxHeapMemory) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, systemInfo.getAvailableProcessors(), systemInfo.getSystemLoadAverage(), systemInfo.getUsedHeapMemory(), systemInfo.getMaxHeapMemory());
    }

    public List<SystemInfo> findAll() {
        String sql = "SELECT * FROM system_info";
        return jdbcTemplate.query(sql, new DataClassRowMapper<>(SystemInfo.class));
    }
}
