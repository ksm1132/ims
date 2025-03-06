package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.OrderDetail;
import com.ims.inventorymgmtsys.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderBillAmountRepository implements OrderBillAmountRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int billAmountCalculate(OrderDetail orderDetail) {
        String sql = "SELECT SUM(od.quantity * p.price) AS total_amount" +
                "FROM t_order_detail od" +
                "JOIN t_product p ON od.id = p.id" +
                "WHERE od.id = ?";

        Object[] param = new Object[]{orderDetail.getOrderId()};

        return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(int.class), param);

    }
}
