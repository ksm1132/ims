package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("JdbcProductRepositoryTest.sql")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JdbcProductRepositoryTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new JdbcProductRepository(jdbcTemplate);
//        jdbcTemplate.update("INSERT INTO t_product (name, price, stock) VALUES (?, ?, ?)","Test Product", 100, 10);
    }

    @Test
    @Transactional
    void test_selectById() {
        Product testProduct = new Product();
        testProduct.setName("testProduct");
        testProduct.setPrice(19800);
        testProduct.setStock(22);
        testProduct.setImgUrl("/iamges/testProduct.jpg");
        productRepository.save(testProduct);

        //Save後、DBからオブジェクトを取得するために一度namdでDBから引っ張る
        Product product2 = productRepository.findByName("testProduct");
        Product product3 = productRepository.findById(product2.getId());
        assertThat(product3.getName()).isEqualTo("testProduct");
        assertThat(product3.getPrice()).isEqualTo(19800);
        assertThat(product3.getStock()).isEqualTo(22);

    }

    @Test
    void test_selelctByName() {
        Product product = productRepository.findByName("モンベル　ライトダウン");
        assertThat(product.getName()).isEqualTo("モンベル　ライトダウン");
        assertThat(product.getPrice()).isEqualTo(12000);
        assertThat(product.getStock()).isEqualTo(50);
    }


    @Test
    void test_selectAll() {
        List<Product> products = productRepository.findAll();
        assertThat(products.size()).isEqualTo(6);
    }

    @Test
    void test_update() {
        Product product = productRepository.findByName("モンベル　ライトダウン");
        product.setName("おばけ");
        product.setPrice(99);
        product.setStock(33);
        boolean result = productRepository.update(product);
        assertThat(result).isEqualTo(true);

        Map<String, Object> productMap = jdbcTemplate.queryForMap(
                "SELECT * FROM t_product WHERE id = ?", product.getId());
        assertThat(productMap.get("name")).isEqualTo("おばけ");
        assertThat(productMap.get("price")).isEqualTo(99);
        assertThat(productMap.get("stock")).isEqualTo(33);
    }

}
