package com.ims.inventorymgmtsys.api;

import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.entity.ProductWrapper;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalog")
public class CatalogApiController {
    private final CatalogService catalogService;

    public CatalogApiController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/productList")
    public List<Product> getAllProductList() {
        return catalogService.findAll();
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable UUID id) {
        return catalogService.findById(id);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable UUID id, @RequestBody Product updateProduct) {
        updateProduct.setId(id);
        boolean success = catalogService.update(updateProduct);
        if (success) {
            return ResponseEntity.ok("Product update successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @PostMapping("/products")
    public ResponseEntity<String> registerProduct(@RequestBody Product newProduct) {
        try {
            catalogService.save(newProduct);
            return new ResponseEntity<>("Product created Successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create product", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
