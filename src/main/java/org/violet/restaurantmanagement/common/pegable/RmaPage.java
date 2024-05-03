package org.violet.restaurantmanagement.common.pegable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
                    .pageNumber(page.getNumber())
                    .pageSize(page.getSize())
                    .totalPageCount(page.getTotalPages())
                    .totalElementCount(page.getTotalElements())
                    .sortedBy(Sorting.toSort(page.getSort()));
            return this;
        }
    }
}
