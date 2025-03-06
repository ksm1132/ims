package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.SystemInfo;

import java.util.List;

public interface SystemInfoRepository {
    void save(SystemInfo systemInfo);
    List<SystemInfo> findAll();
}
