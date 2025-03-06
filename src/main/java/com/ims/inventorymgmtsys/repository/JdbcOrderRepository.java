package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Employee;
import com.ims.inventorymgmtsys.entity.Order;
import com.ims.inventorymgmtsys.entity.OrderProductDTO;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcOrderRepository implements OrderRepository{
    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate){this.jdbcTemplate = jdbcTemplate;}

    @Override
    public void save(Order order) {
        jdbcTemplate.update("INSERT INTO t_order VALUES (?,?,?,?,?,?)",
                order.getOrderId(),
                order.getOrderDateTime(),
                order.getCustomerId(),
                order.getCustomerName(),
                order.getEmployeeName(),
                order.getPaymentMethod().toString()
                );
    }

    @Override
    public List<Order> findAll(){
        return jdbcTemplate.query("SELECT * FROM t_order", new DataClassRowMapper<>(Order.class));
    }

    @Override
    public Order findById(String orderId) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM t_order WHERE id = ?", new BeanPropertyRowMapper<>(Order.class), orderId);
        return orders.isEmpty() ? null : orders.get(0);
    }

    @Override
    public List<OrderProductDTO> findByUserId(UUID customerId) {
        List<OrderProductDTO> orders = jdbcTemplate.query("SELECT subqa.orderid,subqa.order_date_time,subqb.name as productName,subqb.quantity, subqb.price FROM " +
                "( SELECT orderid,order_date_time,customerid FROM T_ORDER WHERE customerid = ? ) as subqa " +
                "JOIN (SELECT tod.order_id,tod.product_id, tp.name,tp.price, tod.quantity FROM t_order_detail tod " +
                "JOIN t_product tp ON tod.product_id = tp.id) as subqb ON subqa.orderid = subqb.order_id;", new BeanPropertyRowMapper<>(OrderProductDTO.class), customerId);

        return orders.isEmpty() ? null : orders;
    }

}
