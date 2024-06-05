package org.violet.restaurantmanagement.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RmaPage<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private int totalPageCount;

    private long totalElementCount;

    private Sorting sortedBy;

    private Filtering filteredBy;

    @SuppressWarnings("unused")
    public static class RmaPageBuilder<T> {

        private RmaPageBuilder() {
        }

        public <Z> RmaPageBuilder<T> page(Page<Z> page) {
            this
                    .pageNumber(page.getNumber() + 1)
                    .pageSize(page.getContent().size())
                    .totalPageCount(page.getTotalPages())
                    .totalElementCount(page.getTotalElements())
                    .sortedBy(Sorting.toSort(page.getSort()));
            return this;
        }
    }
}
