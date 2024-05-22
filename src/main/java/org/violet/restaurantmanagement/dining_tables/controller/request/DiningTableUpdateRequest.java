package org.violet.restaurantmanagement.dining_tables.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;

import java.util.EnumSet;

public record DiningTableUpdateRequest(

        @NotNull
        DiningTableStatus status,

        @NotNull
        @Range(min = 0, max = Integer.MAX_VALUE)
        Integer size
) {
    @JsonIgnore
    @AssertTrue(message = "Status must be either 'VACANT' or 'RESERVED' or 'OCCUPIED' or 'TAKING_ORDERS' or 'DELETED'")
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    private boolean isDiningTableStatusValid() {

        if (this.status == null) {
            return true;
        }

        EnumSet<DiningTableStatus> acceptableStatus = EnumSet.of(
                DiningTableStatus.VACANT,
                DiningTableStatus.RESERVED,
                DiningTableStatus.OCCUPIED,
                DiningTableStatus.TAKING_ORDERS,
                DiningTableStatus.DELETED
        );
        return acceptableStatus.contains(this.status);
    }
}
