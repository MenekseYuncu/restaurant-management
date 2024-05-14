package org.violet.restaurantmanagement.product.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.violet.restaurantmanagement.common.controller.requset.RmaPaginationRequest;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProductListRequest extends RmaPaginationRequest {

    private ProductFilter filter;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductFilter implements Filtering {

        private String name;

        private Long categoryId;

        private Set<ProductStatus> statuses;

        private ProductPriceRange priceRange;
    }

    @Getter
    @AllArgsConstructor
    public static class ProductPriceRange {
        private BigDecimal min;
        private BigDecimal max;
    }
}
