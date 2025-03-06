package com.ims.inventorymgmtsys.entity;

import java.util.List;

public class ProductWrapper {
    private List<Product> products;

    public ProductWrapper() {
        // Default constructor
    }

    public ProductWrapper(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
