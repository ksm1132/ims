package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.entity.ProductWrapper;
import com.ims.inventorymgmtsys.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll(){return productRepository.findAll();}

    @Override   
    public Product findById(UUID id) {return productRepository.findById(id);}

    @Override
    public boolean update(Product product) {
        return productRepository.update(product);
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public ProductWrapper getProductWrapper() {
        List<Product> products = productRepository.findAll();
        return new ProductWrapper(products);
    }

    @Override
    public boolean updateProducts(List<Product> products) {
        boolean isUpdate = false;
            for (Product product : products) {
                Product existingProduct = findById(product.getId());
                if (existingProduct != null) {
                    boolean updated = false;
                    if(!existingProduct.getName().equals(product.getName())){
                        existingProduct.setName(product.getName());
                        updated = true;
                    }
                    if (!existingProduct.getPrice().equals(product.getPrice())){
                        existingProduct.setPrice(product.getPrice());
                        updated = true;
                    }
                    if (!existingProduct.getStock().equals(product.getStock())){
                        existingProduct.setStock(product.getStock());
                        updated = true;
                    }
                    if (!existingProduct.getImgUrl().equals(product.getImgUrl())){
                        existingProduct.setImgUrl(product.getImgUrl());
                        updated = true;
                    }

                    if(updated) {
                        update(existingProduct);
                        isUpdate = true;

                    }

                }
            }
            return isUpdate;

        }

    @Override
    public Product findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> findAllWithPagination(int page, int size) {
        return productRepository.findAllWithPagination(page, size);
    }

    @Override
    public Optional<Integer> getAllProductCount() {
        return productRepository.countAll();
    }


}
