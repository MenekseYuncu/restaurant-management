package org.violet.restaurantmanagement.common.controller.requset;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.violet.restaurantmanagement.common.model.Pagination;
import org.violet.restaurantmanagement.common.model.Sorting;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class RmaPaginationRequest {

    @NotNull
    @Valid
    private Pagination pagination;

    private Sorting sorting;
}
