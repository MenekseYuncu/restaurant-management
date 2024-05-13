package org.violet.restaurantmanagement.product.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;
import org.violet.restaurantmanagement.product.model.enums.ExtentType;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.EnumSet;

public record ProductUpdateRequest(

        @NotBlank
        @Size(min = 2, max = 300)
        String name,

        @NotBlank
        @Size(min = 2)
        String ingredient,

        @NotNull
        @DecimalMin(value = "0", inclusive = false)
        BigDecimal price,

        @NotNull
        ProductStatus status,

        @NotNull
        @Range(min = 0, max = Integer.MAX_VALUE)
        Integer extent,

        @NotNull
        ExtentType extentType
) {
    @JsonIgnore
    @AssertTrue(message = "Status must be either 'ACTIVE' or 'INACTIVE' or 'DELETED'")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isProductStatusValid() {

        if (this.status == null) {
            return true;
        }

        EnumSet<ProductStatus> acceptableStatus = EnumSet.of(
                ProductStatus.ACTIVE,
                ProductStatus.INACTIVE,
                ProductStatus.DELETED
        );
        return acceptableStatus.contains(this.status);
    }

    @JsonIgnore
    @AssertTrue(message = "Extent type must be either 'ML' or 'GR'")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isExtentTypeValid() {

        if (this.extentType == null) {
            return true;
        }

        EnumSet<ExtentType> acceptableStatus = EnumSet.of(
                ExtentType.ML,
                ExtentType.GR
        );
        return acceptableStatus.contains(this.extentType);
    }
}
