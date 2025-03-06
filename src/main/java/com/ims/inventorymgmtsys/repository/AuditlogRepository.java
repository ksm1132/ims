package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Auditlog;

import java.util.List;

public interface AuditlogRepository {
    void save(Auditlog auditlog);

    List<Auditlog> findAll();


}
