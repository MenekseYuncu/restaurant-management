package org.violet.restaurantmanagement.product.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.common.pegable.Pagination;
import org.violet.restaurantmanagement.product.controller.util.CategoryFilter;

@Getter
@Setter
@Builder
public class CategoryListRequest {

    @NotNull
    @Valid
    private Pagination pagination;

    private CategoryFilter filter;
}
