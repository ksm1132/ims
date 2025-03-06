package com.ims.inventorymgmtsys.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class JdbcSalesRepository implements SalesRepository{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcSalesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Map<String, Object>> getSalesData() {
        String sql = "SELECT tod.order_id, SUM(tod.quantity * tp.price) as \"total_yen\" FROM t_order_detail tod " +
                "JOIN t_product tp ON tod.product_id = tp.id " +
                "GROUP BY tod.order_id;";

        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getDailySalesData() {
        String sql = "SELECT to_char(o.order_date_time, 'yyyy-MM-dd') AS date, SUM(jt.total_yen) AS total_date_yen " +
                "FROM t_order o " +
                "JOIN ( " +
                "    SELECT tod.order_id, SUM(tod.quantity * tp.price) AS total_yen " +
                "    FROM t_order_detail tod " +
                "    JOIN t_product tp ON tod.product_id = tp.id " +
                "    GROUP BY tod.order_id " +
                ") jt ON o.orderid = jt.order_id " +
                "GROUP BY to_char(o.order_date_time, 'yyyy-MM-dd');";

        return jdbcTemplate.queryForList(sql);
    }

}
