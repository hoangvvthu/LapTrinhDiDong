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

    private Integer quantity;

    private Integer sold;

    private LocalDate createDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}