package org.violet.restaurantmanagement.common.pegable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
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

    public int getPageNumber(){
        return pageNumber -1;
    }

}
