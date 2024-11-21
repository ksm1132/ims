package com.ims.inventorymgmtsys.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.service.CatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@Sql("/CatalogControllerIntegrationTest.sql")
@Sql("/JdbcProductRepositoryTest.sql")
public class CatalogControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_displayList() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[?(@.name == 'モンベル　ライトダウン')]").exists());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_dispalyDetails() throws Exception {
        String productName = "ASICS マジックスピード4";
        String id = jdbcTemplate.queryForObject("SELECT id FROM t_product WHERE name = ?", String.class , productName);

        mockMvc.perform(get("/catalog/product-details")
                        .param("productId", id)
                )
                .andExpect(content().string(containsString("ASICS マジックスピード4")))
                ;
    }
}
