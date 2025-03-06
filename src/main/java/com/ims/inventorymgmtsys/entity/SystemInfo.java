package com.ims.inventorymgmtsys.entity;

import java.time.LocalDateTime;

public class SystemInfo {
    private Long id;
    private int availableProcessors;
    private double systemLoadAverage;
    private long usedHeapMemory;
    private long maxHeapMemory;
    private LocalDateTime recordedAt;

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public int getAvailableProcessors() {
        return availableProcessors;
    }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public long getUsedHeapMemory() {
        return usedHeapMemory;
    }

    public void setUsedHeapMemory(long usedHeapMemory) {
        this.usedHeapMemory = usedHeapMemory;
    }

    public long getMaxHeapMemory() {
        return maxHeapMemory;
    }

    public void setMaxHeapMemory(long maxHeapMemory) {
        this.maxHeapMemory = maxHeapMemory;
    }

    public LocalDateTime getRecordedAt() {return recordedAt;}

    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt;}


}
