package org.violet.restaurantmanagement.common.model;

import org.springframework.data.domain.Sort;

public class SortingBuilder {

    private Sorting sorting;

    public static SortingBuilder builder() {
        SortingBuilder sortingBuilder = new SortingBuilder();
        sortingBuilder.sorting = Sorting.builder().build();
        return sortingBuilder;
    }

    public Sorting build() {
        return this.sorting;
    }

    public SortingBuilder property(String property) {
        sorting.setProperty(property);
        return this;
    }

    public SortingBuilder asc() {
        sorting.setDirection(Sort.Direction.ASC);
        return this;
    }

    public SortingBuilder desc() {
        sorting.setDirection(Sort.Direction.DESC);
        return this;
    }
}