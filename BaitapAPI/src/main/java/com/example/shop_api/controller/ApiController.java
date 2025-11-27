package com.example.shop_api.controller;

import com.example.shop_api.entity.*;
import com.example.shop_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ProductRepository productRepo;

    // YÊU CẦU 1: Hiển thị tất cả danh mục
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // YÊU CẦU 2: Hiển thị sản phẩm theo từng danh mục
    @GetMapping("/categories/{id}/products")
    public List<Product> getProductsByCategory(@PathVariable Long id) {
        return productRepo.findByCategory_Id(id);
    }

    // YÊU CẦU 3: Hiển thị 10 sản phẩm bán chạy nhất
    @GetMapping("/products/best-sellers")
    public List<Product> getBestSellers() {
        return productRepo.findTop10ByOrderBySoldDesc();
    }

    // YÊU CẦU 4: Hiển thị 10 sản phẩm mới (<= 7 ngày)
    @GetMapping("/products/new-arrivals")
    public List<Product> getNewArrivals() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return productRepo.findTop10ByCreateDateAfterOrderByCreateDateDesc(sevenDaysAgo);
    }
}