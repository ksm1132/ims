package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.OrderDetail;
import com.ims.inventorymgmtsys.entity.Product;

public interface OrderBillAmountRepository {
    int billAmountCalculate(OrderDetail orderDetail);
}
