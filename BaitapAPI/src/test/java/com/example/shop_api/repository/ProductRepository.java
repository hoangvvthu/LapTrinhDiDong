package com.example.shop_api.repository;

import com.example.shop_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 1. Tìm sản phẩm theo ID danh mục
    // Spring sẽ tự hiểu và tạo câu lệnh SQL: SELECT * FROM products WHERE category_id = ?
    List<Product> findByCategory_Id(Long categoryId);

    // 2. Top 10 bán chạy (Sắp xếp theo 'sold' giảm dần, lấy 10 cái đầu)
    List<Product> findTop10ByOrderBySoldDesc();

    // 3. Top 10 sản phẩm mới (Ngày tạo SAU ngày [hiện tại - 7])
    List<Product> findTop10ByCreateDateAfterOrderByCreateDateDesc(LocalDate date);
}