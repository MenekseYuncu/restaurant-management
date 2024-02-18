package org.violet.restaurantmanagement.product.category.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.product.category.model.enums.CategoryStatus;

@Getter
@Setter
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    private CategoryStatus status;
}
