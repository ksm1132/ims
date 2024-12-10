package com.ims.inventorymgmtsys.api;

import com.ims.inventorymgmtsys.entity.Authorities;
import com.ims.inventorymgmtsys.entity.Product;

import static org.assertj.core.api.Assertions.assertThat;

import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.repository.AuthorityRepository;
import com.ims.inventorymgmtsys.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/com/ims/inventorymgmtsys/repository/JdbcProductRepositoryTest.sql")
@Sql(value = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CatalogApiRestControllerIntegrationApServerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserName("user");
        user.setEmailAddress("test@gmail.com");
        user.setPassword(passwordEncoder.encode("abcd"));
        user.setEnabled(true);
        user.setMfaEnabled(false);
        userRepository.save(user);

        Authorities authorities = new Authorities();
        authorities.setUsername("user");
        authorities.setAuthority("ROLE_ADMIN");
        authorityRepository.saveAuthority(authorities);
    }

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void test_getProducts() {
        ResponseEntity<Product[]> responseEntity = testRestTemplate.withBasicAuth("user","abcd").getForEntity("/api/catalog/productList", Product[].class);
        System.out.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product[] products = responseEntity.getBody();
        assertThat(products.length).isEqualTo(7);
        assertThat(products[0].getName()).isEqualTo("モンベル　ライトダウン");
    }


}
