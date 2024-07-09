package org.violet.restaurantmanagement.common.controller.requset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.violet.restaurantmanagement.common.model.Pagination;
import org.violet.restaurantmanagement.common.model.Sorting;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class RmaPaginationRequest {

    @NotNull
    @Valid
    private Pagination pagination;

    private Sorting sorting;


    @JsonIgnore
    public abstract boolean isOrderPropertyAccepted();


    @SuppressWarnings("all")
    public boolean isPropertyAccepted(final Set<String> acceptedProperties) {

        if (this.sorting == null) {
            return true;
        }

        if (StringUtils.isBlank(sorting.getProperty()) || sorting.getDirection() == null) {
            return true;
        }

        return acceptedProperties.contains(sorting.getProperty());
    }
}
