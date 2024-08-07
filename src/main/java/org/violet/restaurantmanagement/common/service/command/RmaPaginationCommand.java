package org.violet.restaurantmanagement.common.service.command;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.violet.restaurantmanagement.common.model.Pagination;
import org.violet.restaurantmanagement.common.model.Sorting;

@Getter
@Setter
@SuperBuilder
public abstract class RmaPaginationCommand {

    private Pagination pagination;

    private Sorting sorting;


    public Pageable toPageable() {

        if (this.sorting != null) {
            return PageRequest.of(
                    this.pagination.getPageNumber() - 1,
                    this.pagination.getPageSize(),
                    Sort.by(Sort.Order.by(this.sorting.getProperty()).with(this.sorting.getDirection()))
            );
        } else {
            return PageRequest.of(
                    pagination.getPageNumber() - 1,
                    pagination.getPageSize()
            );
        }
    }
}
