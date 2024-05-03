package org.violet.restaurantmanagement.product.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.common.pegable.Pagination;
import org.violet.restaurantmanagement.common.pegable.Sorting;
import org.violet.restaurantmanagement.product.service.command.CategoryListCommand;

@Getter
@Builder
public class CategoryListRequest {

    @NotNull
    @Valid
    private Pagination pagination;

    private Sorting sorting;

    private CategoryListCommand.Filter filter;
}
