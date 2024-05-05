package org.violet.restaurantmanagement.common.pegable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public abstract class RmaPaginationCommand {

    private Pagination pagination;

    private Sorting sorting;


    public Pageable toPageable() {

        if (this.sorting != null) {
            return PageRequest.of(
                    this.pagination.getPageNumber(),
                    this.pagination.getPageSize(),
                    Sort.by(Sort.Order.by(this.sorting.getProperty()).with(this.sorting.getDirection()))
            );
        } else {
            return PageRequest.of(
                    pagination.getPageNumber(),
                    pagination.getPageSize()
            );
        }
    }
}
