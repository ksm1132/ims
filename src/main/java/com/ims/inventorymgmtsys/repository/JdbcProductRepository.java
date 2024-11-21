package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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

    @Override
    public boolean update(Product product) {
        int count = jdbcTemplate.update("UPDATE t_product SET name=?, price=?, stock=?, imgUrl=? WHERE id=?",
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getImgUrl(),
                product.getId());

        if (count == 0) {
            return false;
        }
        return true;
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

    @Override
    public List<Product> findAllWithPagination(int page, int size) {
        int offset = page * size;
        String sql = "SELECT * FROM t_product ORDER BY name DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql,
                new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                        ps.setInt(1, size);
                        ps.setInt(2, offset);
                    }
                },
                new DataClassRowMapper<>(Product.class));
    }

    public Optional<Integer> countAll() {
        String sql = "SELECT COUNT(*) FROM t_product";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
            return Optional.ofNullable(count);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


}
