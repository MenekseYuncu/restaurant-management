package org.violet.restaurantmanagement.common.pegable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
@AllArgsConstructor
@Builder
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

    public PageRequest toPageRequest(Sorting sorting) {
        if (sorting != null) {
            return PageRequest.of(getPageNumber(), pageSize, sorting.toSort());
        } else {
            return PageRequest.of(getPageNumber(), pageSize);
        }
    }
}
