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
public class ProductListCommand extends RmaPaginationCommand implements RmaSpecification {

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
    @Builder
    @AllArgsConstructor
    public static class ProductPriceRange {
        private BigDecimal min;
        private BigDecimal max;
    }

    @Override
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    public <C> Specification<C> toSpecification(Class<C> clazz) {
        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (StringUtils.hasText(this.filter.getName())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                            STR."%\{this.filter.getName().toLowerCase()}%"));
        }

        if (this.filter.getCategoryId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("categoryId"), filter.getCategoryId()));
        }

        if (!CollectionUtils.isEmpty(this.filter.getStatuses())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("status").in(this.filter.getStatuses()));
        }

        if (this.filter.getPriceRange() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("price"),
                            this.filter.getPriceRange().getMin(),
                            this.filter.getPriceRange().getMax()
                    ));
        }

        return specification;
    }
}