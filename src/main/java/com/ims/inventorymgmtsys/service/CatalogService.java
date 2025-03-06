package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.CartItemInput;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.UUID;

public interface CatalogService {

    List<Product> findAll();

    Product findById(UUID id);

    CartItemInput createCartItemInput(UUID productId);

    boolean update(Product product);

    void save(Product product);

}
