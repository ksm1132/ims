package com.ims.inventorymgmtsys.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SalesService {

    List<Map<String, Object>> getSalesData();

    void generateSalesChart(HttpServletResponse httpServletResponse) throws IOException;

    List<Map<String, Object>> getDailySalesData();

    void generateDailySalesChart(HttpServletResponse httpServletResponse) throws IOException;

    String getSalesDataAsJson();

    String getDailySalesDataAsJson();
}
