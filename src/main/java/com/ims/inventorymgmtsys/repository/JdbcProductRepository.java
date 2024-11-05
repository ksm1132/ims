package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public class JdbcProductRepository implements ProductRepository{

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public JdbcProductRepository(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public Product findById(UUID id) {
        return jdbcTemplate.queryForObject("SELECT * FROM t_product WHERE id = ?", new DataClassRowMapper<>(Product.class), id);
    }

    @Override
    public Product findByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM t_product WHERE name = ?", new DataClassRowMapper<>(Product.class), name);
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM t_product ORDER BY name DESC", new DataClassRowMapper<>(Product.class));
    }

    @Transactional
    @Override
    public boolean update(Product product) {
        // 行ロックを取得
        jdbcTemplate.queryForObject(
                "SELECT * FROM t_product WHERE id = ? FOR UPDATE",
                new Object[]{product.getId()},
                (rs, rowNum) -> product.getId()  // 必要な情報を返すために、RowMapperを簡単に指定
        );

        int count = jdbcTemplate.update("UPDATE t_product SET name=?, price=?, stock=?, imgUrl=? WHERE id=?",
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getImgUrl(),
                product.getId());

        return count != 0;
    }

    @Override
    public void save(Product product) {
        //idはテーブル定義でUUIDを定義してるため、idは省略
        jdbcTemplate.update("INSERT INTO t_product (name, price, stock, imgUrl) VALUES(?,?,?,?)",
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getImgUrl());
    }


}
