package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.entity.ProductWrapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    List<Product> findAll();

    Product findById(UUID id);

    boolean update(Product product);

    void save(Product product);

    ProductWrapper getProductWrapper();

    boolean updateProducts(List<Product> products);

    Product findByName(String name);

    List<Product> findAllWithPagination(int page, int size);

    Optional<Integer> getAllProductCount();


}
