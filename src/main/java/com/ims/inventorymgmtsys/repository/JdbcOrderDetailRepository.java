package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderDetailRepository implements OrderDetailRepository{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcOrderDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(OrderDetail orderDetail) {
        jdbcTemplate.update("INSERT INTO t_order_detail VALUES (?, ?, ?)",
                orderDetail.getOrderId(),
                orderDetail.getProductId(),
                orderDetail.getQuantity()
                );
    }
}
