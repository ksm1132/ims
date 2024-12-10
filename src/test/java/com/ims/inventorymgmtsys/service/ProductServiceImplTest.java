package com.ims.inventorymgmtsys.service;


import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;


    @Test
    void test_findById() {
        Product product = new Product();
        product.setName("てすと厚底シューズ");
        doReturn(product).when(productRepository).findByName(product.getName());
        Product actual = productService.findByName(product.getName());
        assertThat(actual.getName()).isEqualTo("てすと厚底シューズ");
    }

    @Test
    void test_save() {
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        doNothing().when(productRepository).save(productArgumentCaptor.capture());

        Product product = new Product();
        product.setName("test薄底シューズ");
        product.setPrice(19800);
        product.setStock(20);
        productService.save(product);
        verify(productRepository).save(any());

        Product product2 = productArgumentCaptor.getValue();
        assertThat(product2.getName()).isEqualTo("test薄底シューズ");
        assertThat(product2.getPrice()).isEqualTo(19800);
        assertThat(product2.getStock()).isEqualTo(20);

        verify(productRepository, times(1)).save(any());
    }
}
