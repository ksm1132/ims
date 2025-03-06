package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Test
    @WithMockUser(username = "user", roles = "{USER}")
    void test_validateInput() throws Exception {
        mockMvc.perform(post("/order/orderconfirm")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orderForm"))
                .andExpect(model().attributeHasFieldErrorCode("orderInput","name","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("orderInput", "address", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("orderInput","phone","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("orderInput", "emailAddress","NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("orderInput", "paymentMethod", "NotNull"))
                ;
    }

}
