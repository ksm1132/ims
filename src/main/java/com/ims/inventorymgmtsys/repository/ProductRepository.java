package com.ims.inventorymgmtsys.repository;

import com.ims.inventorymgmtsys.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product findById(UUID id);

    Product findByName(String name);

    List<Product> findAll();

    boolean update(Product product);

    void save(Product product);

    List<Product> findAllWithPagination(int page, int size);

    Optional<Integer> countAll();
}
