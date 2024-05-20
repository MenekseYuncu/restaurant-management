package org.violet.restaurantmanagement.common.model;

public class PaginationBuilder {

    private Pagination pagination;

    public static PaginationBuilder builder() {
        PaginationBuilder paginationBuilder = new PaginationBuilder();
        paginationBuilder.pagination = Pagination.builder().build();
        return paginationBuilder;
    }

    public Pagination build() {
        return this.pagination;
    }

    public PaginationBuilder pageNumber(int pageNumber) {
        this.pagination.setPageNumber(pageNumber);
        return this;
    }

    public PaginationBuilder pageSize(int pageSize) {
        this.pagination.setPageSize(pageSize);
        return this;
    }
}