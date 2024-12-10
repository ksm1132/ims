package com.ims.inventorymgmtsys.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventorymgmtsys.entity.SystemInfo;
import com.ims.inventorymgmtsys.repository.SystemInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;


@ExtendWith(MockitoExtension.class)
public class SystemInfoSeriviceImplTest {

    @InjectMocks
    SystemInfoServiceImpl systemInfoService;

    @Mock
    SystemInfoRepository systemInfoRepository;

    @Mock
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = Mockito.spy(new ObjectMapper());
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void test_recordSystemInfo() {
        systemInfoService.recordSystemInfo();

        verify(systemInfoRepository, times(1)).save(any(SystemInfo.class));
    }

    @Test
    void test_findAll() {
        List<SystemInfo> systemInfoList = List.of(new SystemInfo(), new SystemInfo());
        doReturn(systemInfoList).when(systemInfoRepository).findAll();

        List<SystemInfo> result = systemInfoService.findAll();

        assertEquals(2, result.size());
        verify(systemInfoRepository, times(1)).findAll();
    }

    @Test
    void test_getSystemInfoData() {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setId(1L);
        systemInfo.setAvailableProcessors(4);
        systemInfo.setSystemLoadAverage(2);
        systemInfo.setUsedHeapMemory(1024L);
        systemInfo.setMaxHeapMemory(2048L);
        systemInfo.setRecordedAt(LocalDateTime.now());

        List<SystemInfo> systemInfoList = List.of(systemInfo);
        doReturn(systemInfoList).when(systemInfoRepository).findAll();

        List<Map<String, Object>> result = systemInfoService.getSystemInfoData();

        assertThat(result.get(0).get("availableProcessors")).isEqualTo(4);
        verify(systemInfoRepository,times(1)).findAll();

    }

    @Test
    void test_getSystemInfoDataAsJson() throws JsonProcessingException {
        SystemInfo systemInfo = new SystemInfo();
        systemInfo.setId(1L);
        systemInfo.setAvailableProcessors(4);
        systemInfo.setSystemLoadAverage(2.0);
        systemInfo.setUsedHeapMemory(1024L);
        systemInfo.setMaxHeapMemory(2048L);
        systemInfo.setRecordedAt(LocalDateTime.parse("2024-12-09T12:00:00"));

        ObjectMapper objectMapperTest = new ObjectMapper();
        objectMapperTest.registerModule(new JavaTimeModule());
        objectMapperTest.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String requestJson = objectMapperTest.writeValueAsString(systemInfo);

        String expectedJson = "{\"id\":1,\"availableProcessors\":4,\"systemLoadAverage\":2.0," +
                "\"usedHeapMemory\":1024,\"maxHeapMemory\":2048," +
                "\"recordedAt\":\"2024-12-09T12:00:00\"}";

        assertEquals(requestJson, expectedJson);
    }
}
