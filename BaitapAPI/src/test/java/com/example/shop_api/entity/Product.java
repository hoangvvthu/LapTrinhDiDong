package com.example.shop_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;

    private Integer quantity; // Số lượng tồn kho

    private Integer sold; // Số lượng ĐÃ BÁN (để làm câu Top 10 bán chạy)

    private LocalDate createDate; // Ngày tạo (để làm câu Top 10 mới nhất)

    @ManyToOne
    @JoinColumn(name = "category_id") // Khóa ngoại liên kết với bảng Category
    private Category category;
}