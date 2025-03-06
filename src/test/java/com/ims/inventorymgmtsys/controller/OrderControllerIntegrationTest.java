package com.ims.inventorymgmtsys.controller;

import com.ims.inventorymgmtsys.entity.Employee;
import com.ims.inventorymgmtsys.entity.Order;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.enumeration.PaymentMethod;
import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.input.OrderInput;
import com.ims.inventorymgmtsys.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Component;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/JdbcProductRepositoryTest.sql")
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testOrderForm() throws Exception {
        mockMvc.perform(get("/order/orderform"))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orderForm"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("orderInput"));
    }


    //以下はIDが指定できないため、失敗する。他にやり方ないか要確認
//    @Test
//    @WithMockUser(username = "user", roles = "USER")
//    public void test_order() throws Exception {
//        OrderInput orderInput = new OrderInput();
//        orderInput.setEmployeeName("佐藤");
//        orderInput.setName("テスト太郎");
//        orderInput.setEmailAddress("test@gmail.com");
//        orderInput.setAddress("南青山");
//        orderInput.setPhone("09012345678");
//        orderInput.setPaymentMethod(PaymentMethod.BANK);
//
//        List<CartItemInput> cartItemInputs = new ArrayList<>();
//
//        CartItemInput testItem = new CartItemInput();
//        testItem.setProductName("消しゴム");
//        testItem.setProductPrice(9999);
//        testItem.setQuantity(88);
//        cartItemInputs.add(testItem);
//
//        CartItemInput testItem2 = new CartItemInput();
//        testItem2.setProductName("テスト肉まん");
//        testItem2.setProductPrice(7777);
//        testItem2.setQuantity(66);
//        cartItemInputs.add(testItem2);
//
//        CartItemInput testItem3 = new CartItemInput();
//        testItem3.setProductName("テストクッキー");
//        testItem3.setProductPrice(4444);
//        testItem3.setQuantity(22);
//        cartItemInputs.add(testItem3);
//
//        CartInput cartInput = new CartInput();
//        cartInput.setCartItemInputs(cartItemInputs);
//
//        Employee employee = jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employeename = ?" , new DataClassRowMapper<>(Employee.class), "佐藤");
//
//        SessionController sessionController = new SessionController();
//        sessionController.setEmployee(employee);
//        sessionController.setOrderInput(orderInput);
//        sessionController.setCartInput(cartInput);
//
//        MvcResult mvcResult = mockMvc.perform(
//                post("/order/placeorder")
//                        .param("placeorderconfirm", "")
//                        .with(csrf())
//                        .sessionAttr("scopedTarget.sessionController", sessionController)
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("order/orderCompletion"))
//                .andReturn();
//
//        Order order = (Order) mvcResult.getFlashMap().get("order");
//
//        Map<String, Object> orderMap = jdbcTemplate.queryForMap("SELECT * FROM t_order WHERE orderid = ?", order.getOrderId());
//        assertThat(orderMap.get("customer_name")).isEqualTo("テスト太郎");
//        assertThat(orderMap.get("payment_method")).isEqualTo(PaymentMethod.BANK);
//
//        User user = (User) mvcResult.getFlashMap().get("user");
//        Map<String, Object> userMap = jdbcTemplate.queryForMap("SELECT * FROM t_user WHERE id = ?", user.getId());
//        assertThat(userMap.get("emailAddress")).isEqualTo("test@gmail.com");
//        assertThat(userMap.get("address")).isEqualTo("南青山");
//        assertThat(userMap.get("phone")).isEqualTo("09012345678");
//
//        int orderItemCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM t_order_detail WHERE orderid = ?", Integer.class, order.getOrderId());
//        assertThat(orderItemCount).isEqualTo(3);
//    }


}
