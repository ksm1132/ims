package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.input.CartInput;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.repository.ProductRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;

    public CatalogServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(UUID id) {
        return productRepository.findById(id);
    }

    @Override
    public CartItemInput createCartItemInput(UUID productId) {
        CartItemInput cartItemInput = new CartItemInput();
        cartItemInput.setQuantity(1);
        cartItemInput.setProductId(productId);
        return cartItemInput;
    }

    @Override
    public boolean update(Product product) {
        return productRepository.update(product);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }
}
