package com.ims.inventorymgmtsys.entity;

import com.ims.inventorymgmtsys.enumeration.PaymentMethod;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order implements Serializable {

    private String orderId; //PK
    private List<OrderDetail> orderDetails;
    private LocalDateTime orderDateTime;

    private UUID customerId; //FK

    private String customerName; //FK
    private String employeeName; //FK

    private PaymentMethod paymentMethod;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderDetail> getOrderDetails() { return orderDetails; }

    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public UUID getCustomerId() { return customerId; }

    public void setCustomerId(UUID customerId) { this.customerId = customerId; }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }


}
