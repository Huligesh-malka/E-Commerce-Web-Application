package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // allows frontend access
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    // ✅ Get one product by ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getById(id);
    }

    // ✅ Add or update product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    // ✅ Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    // ✅ Run this to reassign images and brand info for all products
    @GetMapping("/enrich")
    public ResponseEntity<String> enrichProducts() {
        productService.enrichExistingProducts();
        return ResponseEntity.ok("✅ All products updated with correct images and details!");
    }
}
