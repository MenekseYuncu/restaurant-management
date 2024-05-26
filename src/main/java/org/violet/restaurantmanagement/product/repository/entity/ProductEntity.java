package org.violet.restaurantmanagement.product.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.violet.restaurantmanagement.common.repository.entity.BaseEntity;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rm_product")
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "ingredient")
    private String ingredient;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(name = "extent")
    private Integer extent;

    @Column(name = "extent_type")
    @Enumerated(EnumType.STRING)
    private ExtentType extentType;

    public void delete() {
        this.status = ProductStatus.DELETED;
    }
}
