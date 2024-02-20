package org.violet.restaurantmanagement.product.category.model.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.violet.restaurantmanagement.product.category.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.category.model.enums.ProductStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rm_category")
public class ProductEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid")
    private String id;
    @Column(name = "extent")
    private Integer extent;
    @Column(name = "name")
    private String name;
    @Column(name = "ingredient")
    private String ingredient;
    @Column(name = "price")
    private double price;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "extent_type")
    private ExtentType type;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "categoryid")
    private CategoryEntity categoryEntity;

    public void setCategory(CategoryEntity category){
        this.categoryEntity = category;
        this.categoryEntity.addProduct(this);
    }
}
