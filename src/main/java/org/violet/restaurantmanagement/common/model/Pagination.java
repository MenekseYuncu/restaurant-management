package org.violet.restaurantmanagement.common.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Pagination {

    @Positive
    @NotNull
    @Range(min = 1, max = Integer.MAX_VALUE)
    private int pageNumber;

    @Positive
    @NotNull
    @Range(min = 1, max = Integer.MAX_VALUE)
    private int pageSize;
}
