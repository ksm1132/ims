package com.ims.inventorymgmtsys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventorymgmtsys.entity.Auditlog;
import com.ims.inventorymgmtsys.repository.AuditlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuditlogServiceImpl implements AuditlogService{

    private final AuditlogRepository auditlogRepository;
    private final ObjectMapper objectMapper;

    public AuditlogServiceImpl(AuditlogRepository auditlogRepository, ObjectMapper objectMapper) {
        this.auditlogRepository = auditlogRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(Auditlog auditlog) {
        auditlogRepository.save(auditlog);
    }

    @Override
    public List<Auditlog> findAll() {
       return auditlogRepository.findAll();
    }

    @Override
    public String findAllAsJson() {
        List<Auditlog> auditlogs = findAll();
        try {
            return objectMapper.writeValueAsString(auditlogs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting audit log to JSON", e);
        }

    }

}
