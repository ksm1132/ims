package com.ims.inventorymgmtsys.entity;


import java.io.Serializable;

public class DeliverItem implements Serializable {
    private String deliverId; //FK
    private String productId; //FK
    private Integer quantity;

    public String getDeliverId() {
        return deliverId;
    }


    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
