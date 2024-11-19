package com.ims.inventorymgmtsys.api;

import com.ims.inventorymgmtsys.entity.Product;
import com.ims.inventorymgmtsys.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/products")
    public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        // ページネーションをサポートするメソッドを呼び出す
        List<Product> products = productService.findAllWithPagination(page, size);
        Optional<Integer> totalProductCount = productService.getAllProductCount();  // 商品数を取得

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);  // 商品リスト
        response.put("totalProductCount", totalProductCount.orElse(0));  // 総商品数
        response.put("page", page);  // 現在のページ
        response.put("size", size);  // ページサイズ

        return ResponseEntity.ok(response);
    }

}
