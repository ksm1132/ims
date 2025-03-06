package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CatalogController.class)
public class CatalogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CatalogService catalogService;

    @Test
    @WithMockUser(username = "user", roles = "{USER}")
    void test_displayList() throws Exception {
        mockMvc.perform(get("/catalog/list").param("name","Patagonia フリース"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/productList"))
                .andExpect(content().string(containsString("商品一覧")));
    }
}
