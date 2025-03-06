package com.ims.inventorymgmtsys.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventorymgmtsys.service.CatalogService;
import com.ims.inventorymgmtsys.service.CatalogServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/JdbcProductRepositoryTest.sql")
@Transactional
public class CatalogApiRestControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    CatalogServiceImpl catalogService;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void test_getProducts() throws Exception {
        mockMvc.perform(get("/api/catalog/productList")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("モンベル　ライトダウン"))
                .andExpect(jsonPath("$[0].price").value(12000))
                .andExpect(jsonPath("$[0].stock").value(50))
                ;
    }


}
