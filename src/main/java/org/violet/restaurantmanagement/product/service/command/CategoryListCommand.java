package org.violet.restaurantmanagement.product.service.command;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.common.pegable.Pagination;
import org.violet.restaurantmanagement.common.pegable.Sorting;
import org.violet.restaurantmanagement.product.controller.util.CategoryFilter;

@Getter
@Builder
public class CategoryListCommand {

    @NotNull
    private Pagination pagination;

    private CategoryFilter filter;

    private Sorting sorting;
}
