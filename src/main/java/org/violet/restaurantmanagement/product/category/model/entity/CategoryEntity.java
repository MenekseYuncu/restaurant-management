package org.violet.restaurantmanagement.product.category.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.product.category.model.enums.CategoryStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "rm_category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany
    private List<ProductEntity> products;

    public void addProduct(ProductEntity product){
        if(this.products == null) products = new ArrayList<>();
        products.add(product);
    }
}
