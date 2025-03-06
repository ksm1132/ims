package com.ims.inventorymgmtsys.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ims.inventorymgmtsys.config.CustomAuthenticationFailureHandler;
import com.ims.inventorymgmtsys.config.CustomAuthenticationSuccessHandler;
import com.ims.inventorymgmtsys.config.SecurityConfig;
import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.entity.ProductWrapper;
import com.ims.inventorymgmtsys.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class CatalogAdminControllerSecurityTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CatalogService catalogService;

    @MockBean
    SessionController sessionController;

    @MockBean
    AuditlogService auditlogService;

    @MockBean
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @MockBean
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @MockBean
    LoginUserDetailService loginUserDetailService;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    OrderService orderService;

    @MockBean
    ProductService productService;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    SalesService salesService;

    @MockBean
    ProductWrapper productWrapper;

    @Test
    @WithMockUser(username = "user", roles = "ADMIN")
    void test_displayListAdminable() throws Exception {
        ProductWrapper productWrapper = new ProductWrapper();
        productWrapper.setProducts(new ArrayList<>());

        when(productService.getProductWrapper()).thenReturn(productWrapper);
        when(orderService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/admin/management")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("admin/adminManagement"))
                .andExpect(model().attributeExists("productWrapper"))
                .andExpect(model().attributeExists("orderList"))
                .andExpect(model().attributeExists("newProduct"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void test_displayListUserUnable() throws Exception {
        ProductWrapper productWrapper = new ProductWrapper();
        productWrapper.setProducts(new ArrayList<>());

        when(productService.getProductWrapper()).thenReturn(productWrapper);
        when(orderService.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/admin/management")
                )
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/access-denied"));
//                .andExpect(view().name("admin/adminManagement"))
//                .andExpect(model().attributeExists("productWrapper"))
//                .andExpect(model().attributeExists("orderList"))
//                .andExpect(model().attributeExists("newProduct"));
    }
}
