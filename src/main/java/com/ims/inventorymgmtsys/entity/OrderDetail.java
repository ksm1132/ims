package com.ims.inventorymgmtsys.entity;

import java.io.Serializable;
import java.util.UUID;

public class OrderDetail implements Serializable {
    private String orderId; //FK
    private UUID productId; //FK

    private Integer quantity;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) { this.orderId = orderId; }


    public UUID getProductId() {
        return productId;
    }
    public void setProductId(UUID productId) { this.productId = productId;}

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
