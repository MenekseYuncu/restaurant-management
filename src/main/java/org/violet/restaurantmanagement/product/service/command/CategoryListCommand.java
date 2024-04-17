package org.violet.restaurantmanagement.product.service.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.common.pegable.Pagination;
import org.violet.restaurantmanagement.common.pegable.Sorting;
import org.violet.restaurantmanagement.product.controller.util.CategoryFilter;

@Getter
@Setter
@Builder
public class CategoryListCommand {

    private Pagination pagination;

    private CategoryFilter filter;

    private Sorting sorting;
}
