package com.ims.inventorymgmtsys.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;
import java.util.UUID;

import static net.snowflake.client.jdbc.internal.apache.arrow.flatbuf.Type.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatalogApiController.class)
public class CatalogApiRestController {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CatalogService catalogService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_CatalogApi() throws Exception {
        CartItemInput cartItemInput = new CartItemInput();
        cartItemInput.setProductName("testaaaaaaaaaa");
        cartItemInput.setProductPrice(87699);
        cartItemInput.setQuantity(999);
        String requestBody = objectMapper.writeValueAsString(cartItemInput);

        mockMvc.perform(post("/api/catalog/products")
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_getProducts() throws Exception {
        Product product = new Product();
        product.setName("testaaaaaaaaaa");
        product.setPrice(87699);
        product.setStock(999);
        UUID productId = UUID.randomUUID();
        when(catalogService.findById(productId)).thenReturn(product);

        String responseBody = mockMvc.perform(get("/api/catalog/products/{id}", productId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testaaaaaaaaaa"))
                .andExpect(jsonPath("$.price").value(87699))
                .andExpect(jsonPath("$.stock").value(999))
                .andReturn().getResponse().getContentAsString(Charset.forName("UTF-8"));

        String json = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(objectMapper.readTree(responseBody));
        System.out.println(json);
    }

}
