package org.violet.restaurantmanagement.product.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

import java.util.EnumSet;

public record CategoryUpdateRequest(
        @NotBlank
        @Size(min = 2, max = 300)
        String name,
        @NotNull
        CategoryStatus status
) {

    @JsonIgnore
    @AssertTrue(message = "Status must be either 'ACTIVE' or 'INACTIVE' or 'DELETED'")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isEmployeeRoleValid() {

        if (this.status == null) {
            return true;
        }

        EnumSet<CategoryStatus> acceptableStatus = EnumSet.of(
                CategoryStatus.ACTIVE,
                CategoryStatus.INACTIVE,
                CategoryStatus.DELETED

        );
        return acceptableStatus.contains(this.status);
    }
}