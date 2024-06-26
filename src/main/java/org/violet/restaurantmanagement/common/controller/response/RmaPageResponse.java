package org.violet.restaurantmanagement.common.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.common.model.Sorting;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RmaPageResponse<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private int totalPageCount;

    private long totalElementCount;

    private Sorting sortedBy;

    private Filtering filteredBy;

    @SuppressWarnings("unused")
    public static class RmaPageResponseBuilder<T> {

        private RmaPageResponseBuilder() {
        }

        public <Z> RmaPageResponseBuilder<T> page(RmaPage<Z> page) {
            this
                    .pageNumber(page.getPageNumber())
                    .pageSize(page.getPageSize())
                    .totalPageCount(page.getTotalPageCount())
                    .totalElementCount(page.getTotalElementCount())
                    .sortedBy(page.getSortedBy())
                    .filteredBy(page.getFilteredBy());
            return this;
        }
    }
}
