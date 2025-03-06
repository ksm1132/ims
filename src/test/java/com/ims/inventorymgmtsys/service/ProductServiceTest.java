package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/JdbcProductRepositoryTest.sql")
@Transactional
public class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @Test
    void test_findById() {
        Product product = productService.findByName("コンバース　ジャックパーセル");
        assertThat(product.getStock()).isEqualTo(70);
    }

    @Test
    void test_findAll() {
        List<Product> products = productService.findAll();
        assertThat(products.get(0).getName()).isEqualTo("モンベル　ライトダウン");
    }

    @Test
    void test_update() {
        List<Product> products = productService.findAll();
        Map<String, Object> productMap = jdbcTemplate.queryForMap("SELECT * FROM t_product WHERE id = ?", products.get(0).getId());
        assertThat(productMap.get("name")).isEqualTo("モンベル　ライトダウン");
        assertThat(productMap.get("price")).isEqualTo(12000);
        assertThat(productMap.get("stock")).isEqualTo(50);
    }
}
