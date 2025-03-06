package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Auditlog;

import java.util.List;

public interface AuditlogService {
    void save(Auditlog auditlog);
    List<Auditlog> findAll();
    String findAllAsJson();

}
