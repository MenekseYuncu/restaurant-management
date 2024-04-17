package org.violet.restaurantmanagement.common.pegable;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Builder
public class Sorting {

    @NotNull
    public String orderBy;

    @NotNull
    public Sort.Direction order;
}
