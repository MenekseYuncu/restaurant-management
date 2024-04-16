package org.violet.restaurantmanagement.common.pegable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
public class Pagination {

    @Positive
    @NotNull
    @Range(min = 1, max = Integer.MAX_VALUE)
    public int pageNumber;

    @Positive
    @NotNull
    @Range(min = 1, max = Integer.MAX_VALUE)
    public int pageSize;
}
