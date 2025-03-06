package com.ims.inventorymgmtsys.repository;

import java.util.List;
import java.util.Map;

public interface SalesRepository {

    List<Map<String, Object>> getSalesData();

    List<Map<String, Object>> getDailySalesData();
}
