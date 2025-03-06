package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.SystemInfo;

import java.util.List;
import java.util.Map;

public interface SystemInfoService {
    void recordSystemInfo();

    List<SystemInfo> findAll();

    List<Map<String, Object>> getSystemInfoData();

    String getSystemInfoDataAsJson();
}
