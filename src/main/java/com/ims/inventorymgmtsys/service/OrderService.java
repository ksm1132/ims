package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Employee;
import com.ims.inventorymgmtsys.entity.Order;
import com.ims.inventorymgmtsys.entity.OrderProductDTO;
import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.input.OrderInput;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    int calculateTotalAmount(List<CartItemInput> cartItemInputs);
    int calculateTax(int price);
    Order placeOrder(OrderInput orderInput, CartInput cartInput, Employee employee);

    Order findById(String orderId);

    List<Order> findAll();

    List<Employee> findAllEmployees();

    Employee findEmployeeById(UUID employeeId);

    CartInput getCartInput();
    void setCartInput(CartInput cartInput);
    OrderInput getOrderInput();
    void setOrderInput(OrderInput orderInput);

    void clearSessionData();

    List<OrderProductDTO> getOrderListForCurrentUser();

}
