package org.violet.restaurantmanagement.common.pegable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageContent<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private int totalPageCount;

    private long totalElementCount;

    private List<Sorting> sortedBy;

    private Filtering filteredBy;
}
