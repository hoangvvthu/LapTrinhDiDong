package com.example.shop_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<Product> products;
}