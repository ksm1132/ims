package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.controller.SessionController;
import com.ims.inventorymgmtsys.entity.*;
import com.ims.inventorymgmtsys.exception.StockShortageException;
import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.input.OrderInput;
import com.ims.inventorymgmtsys.repository.*;
import com.ims.inventorymgmtsys.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    private final EmployeeRepository employeeRepository;

    private final SessionController sessionController;

    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;


    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository, OrderDetailRepository orderDetailRepository, EmployeeRepository employeeRepository, SessionController sessionController, UserRepository userRepository, SecurityUtils securityUtils) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.employeeRepository = employeeRepository;
        this.sessionController = sessionController;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public Order placeOrder(OrderInput orderInput, CartInput cartInput, Employee employee) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setOrderDateTime(LocalDateTime.now());
        order.setCustomerId(securityUtils.getCurrentId());
        order.setCustomerName(orderInput.getName());
        order.setEmployeeName(employee.getEmployeeName());
        order.setPaymentMethod(sessionController.getOrderInput().getPaymentMethod());

        int totalAmount = calculateTotalAmount(cartInput.getCartItemInputs());
        int billingAmount = calculateTax(totalAmount);

        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemInput cartItem : cartInput.getCartItemInputs()) {
            Product product = productRepository.findById(cartItem.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product not found with ID: " + cartItem.getProductId());
            }

            // 在庫を更新
            int afterstock = product.getStock() - cartItem.getQuantity();
            if (afterstock < 0) {
                throw new StockShortageException("在庫が足りません");
            }
            product.setStock(afterstock);

            // Productを保存
            productRepository.update(product);

            // OrderDetailを作成し保存
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setProductId(product.getId());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetailRepository.save(orderDetail);

            // OrderDetailリストに追加
            orderDetails.add(orderDetail);
        }
        order.setOrderDetails(orderDetails);
        return order;
    }


    @Override
    public int calculateTotalAmount(List<CartItemInput> cartItemInputs) {
        int totalAmount = 0;
        for (CartItemInput cartItem : cartItemInputs) {
            totalAmount += (cartItem.getProductPrice() * cartItem.getQuantity());
        }
        return totalAmount;
    }

    @Override
    public int calculateTax(int price) {
        return (int) (price * 1.1);
    }

    @Override
    public Order findById(String orderId) { return orderRepository.findById(orderId); }

    @Override
    public List<Order> findAll() { return orderRepository.findAll(); }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findEmployeeById(UUID employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @Override
    public CartInput getCartInput() {
        return sessionController.getCartInput();
    }
    @Override
    public void setCartInput(CartInput cartInput) {
        sessionController.setCartInput(cartInput);
    }

    @Override
    public OrderInput getOrderInput() {
        return sessionController.getOrderInput();
    }

    @Override
    public void setOrderInput(OrderInput orderInput) {
        sessionController.setOrderInput(orderInput);
    }

    @Override
    public void clearSessionData(){
        sessionController.clearData();
    }

    @Override
    public List<OrderProductDTO> getOrderListForCurrentUser() {
        return orderRepository.findByUserId(securityUtils.getCurrentId());
    }

}
