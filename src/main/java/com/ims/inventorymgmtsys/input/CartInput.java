package com.ims.inventorymgmtsys.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Serial")
public class CartInput implements Serializable {
    private Integer totalAmount;
    private Integer billingAmount;
    private List<CartItemInput> cartItemInputs;

    public CartInput() {
        this.cartItemInputs = new ArrayList<>();
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(Integer billingAmount) {
        this.billingAmount = billingAmount;
    }

    public List<CartItemInput> getCartItemInputs() {
        return cartItemInputs;
    }

    public void setCartItemInputs(List<CartItemInput> cartItemInputs) {
        this.cartItemInputs = cartItemInputs;
    }
}