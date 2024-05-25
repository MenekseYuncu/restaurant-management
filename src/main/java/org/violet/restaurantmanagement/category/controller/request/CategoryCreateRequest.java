package org.violet.restaurantmanagement.category.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;

import java.util.EnumSet;

public record CategoryCreateRequest(

        @NotBlank
        @Size(min = 2, max = 300)
        String name,

        CategoryStatus status
) {

        @JsonIgnore
        @AssertTrue(message = "Status must be either 'ACTIVE' or 'INACTIVE'")
        @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
        private boolean isCategoryStatusValid() {

                if (this.status == null) {
                        return true;
                }

                EnumSet<CategoryStatus> acceptableStatus = EnumSet.of(
                        CategoryStatus.ACTIVE,
                        CategoryStatus.INACTIVE
                );
                return acceptableStatus.contains(this.status);
        }
}
