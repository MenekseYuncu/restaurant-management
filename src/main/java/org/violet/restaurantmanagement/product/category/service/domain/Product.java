package org.violet.restaurantmanagement.product.category.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.product.category.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.category.model.enums.ProductStatus;

@Getter
@Setter
@AllArgsConstructor
public class Product {

    private String id;
    private String name;
    private Integer extent;
    private String ingredient;
    private Double price;
    private ProductStatus status;
    private ExtentType type;
}
