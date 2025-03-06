package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Employee;
import com.ims.inventorymgmtsys.entity.Order;
import com.ims.inventorymgmtsys.entity.OrderProductDTO;

import java.util.List;
import java.util.UUID;

public interface OrderRepository {
    void save(Order order);

    List<Order> findAll();

    Order findById(String orderId);

    List<OrderProductDTO> findByUserId(UUID customerId);
}
