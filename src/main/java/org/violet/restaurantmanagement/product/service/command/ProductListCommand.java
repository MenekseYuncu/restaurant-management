package org.violet.restaurantmanagement.product.service.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.common.model.mapper.RmaSpecification;
import org.violet.restaurantmanagement.common.service.command.RmaPaginationCommand;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@SuperBuilder
public class ProductListCommand  extends RmaPaginationCommand implements RmaSpecification {

    private ProductFilter filter;

    @Builder
    @Getter
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

    @Override
    public <C> Specification<C> toSpecification(Class<C> clazz) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (StringUtils.hasText(this.filter.getName())) {
            Specification<C> tempSpecification = (root, _, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), STR."%\{filter.getName().toLowerCase()}%");
            specification = specification.and(tempSpecification);
        }

        if (this.filter.getCategoryId() != null) {
            Specification<C> categorySpecification = (root, _, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("categoryId"), this.filter.getCategoryId());
            specification = specification.and(categorySpecification);
        }

        if (!CollectionUtils.isEmpty(this.filter.getStatuses())) {
            Specification<C> statusSpecification = (root, _, _) ->
                    root.get("status").in(this.filter.getStatuses());
            specification = specification.and(statusSpecification);
        }

        if (this.filter.getPriceRange() != null) {
            BigDecimal minPrice = this.filter.getPriceRange().getMin();
            BigDecimal maxPrice = this.filter.getPriceRange().getMax();
            Specification<C> priceSpecification = (root, _, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            specification = specification.and(priceSpecification);
        }

        return specification;
    }
}
