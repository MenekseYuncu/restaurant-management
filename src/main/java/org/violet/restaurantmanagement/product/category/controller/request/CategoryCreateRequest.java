package org.violet.restaurantmanagement.product.category.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.violet.restaurantmanagement.product.category.model.enums.CategoryStatus;

public record CategoryCreateRequest(

        @NotBlank
        @Size(min = 2, max = 300)
        String name,
        @NotNull
        @Pattern(
                regexp = "^(ACTIVE|INACTIVE)$",
                message = "Status must be either 'ACTIVE' or 'INACTIVE'"
        )
        CategoryStatus status
) {

}
