package com.ims.inventorymgmtsys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventorymgmtsys.entity.SystemInfo;
import com.ims.inventorymgmtsys.repository.SystemInfoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SystemInfoServiceImpl implements SystemInfoService{

    private final SystemInfoRepository systemInfoRepository;
    private final ObjectMapper objectMapper;

    public SystemInfoServiceImpl ( SystemInfoRepository systemInfoRepository, ObjectMapper objectMapper) {
        this.systemInfoRepository = systemInfoRepository;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 60000)
    public void recordSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setAvailableProcessors(Runtime.getRuntime().availableProcessors());
        systemInfo.setSystemLoadAverage(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
        systemInfo.setUsedHeapMemory(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        systemInfo.setMaxHeapMemory(Runtime.getRuntime().maxMemory());

        systemInfoRepository.save(systemInfo);
    }

    @Override
    public List<SystemInfo> findAll() {
        return systemInfoRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> getSystemInfoData() {
        return findAll().stream().map(systemInfo -> { Map<String, Object> map = Map.of(
                "id", systemInfo.getId(),
                "availableProcessors", systemInfo.getAvailableProcessors(),
                "systemLoadAverage", systemInfo.getSystemLoadAverage(),
                "usedHeapMemory", systemInfo.getUsedHeapMemory(),
                "maxHeapMemory", systemInfo.getMaxHeapMemory(),
                "timestamp", systemInfo.getRecordedAt().toString()
                );
                return map;
        })
                .collect(Collectors.toList());

    }


    @Override
    public String getSystemInfoDataAsJson() {
        try {
            return objectMapper.writeValueAsString(getSystemInfoData());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert system info data to JSON", e);
        }
    }


}
