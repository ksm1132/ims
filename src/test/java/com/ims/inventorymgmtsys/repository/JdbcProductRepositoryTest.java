package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("/JdbcProductRepositoryTest.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcProductRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new JdbcProductRepository(jdbcTemplate);
        jdbcTemplate.update("INSERT INTO t_product (name, price, stock) VALUES (?, ?, ?)","Test Product", 100, 10);
    }

    @Test
    void test_selectById() {
        Product product = productRepository.findByName("モンベル　ライトダウン");
        assertThat(product.getName()).isEqualTo("モンベル　ライトダウン");
        assertThat(product.getPrice()).isEqualTo(12000);
        assertThat(product.getStock()).isEqualTo(50);
    }

    Product findByName() {
        Product product = productRepository.findByName("モンベル　ライトダウン");
        return product;
    }

    @Test
    void test_selectAll() {
        List<Product> products = productRepository.findAll();
        assertThat(products.size()).isEqualTo(8);
    }

    @Test
    void test_update() {
        Product training = new Product();
        Product product = this.findByName();
        training.setId(product.getId());
        training.setName("おばけ");
        training.setPrice(99);
        training.setStock(33);
        boolean result = productRepository.update(training);
        assertThat(result).isEqualTo(true);

        Map<String, Object> trainingMap = jdbcTemplate.queryForMap(
                "SELECT * FROM t_product WHERE id = ?", product.getId());
        assertThat(trainingMap.get("name")).isEqualTo("おばけ");
        assertThat(trainingMap.get("price")).isEqualTo(99);
        assertThat(trainingMap.get("stock")).isEqualTo(33);
    }

}
