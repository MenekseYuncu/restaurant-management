package org.violet.restaurantmanagement.menu.service.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.common.model.Sorting;
import org.violet.restaurantmanagement.common.model.mapper.RmaSpecification;
import org.violet.restaurantmanagement.common.service.command.RmaPaginationCommand;
import org.violet.restaurantmanagement.product.model.enums.ProductStatus;

@Getter
@SuperBuilder
public class MenuListCommand extends RmaPaginationCommand implements RmaSpecification {

    private MenuFilter filter;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MenuFilter implements Filtering {

        private String name;

    }

    @Override
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    public <C> Specification<C> toSpecification(Class<C> clazz) {
        if (this.filter == null) {
            return Specification.allOf();
        }

        Sorting sorting = Sorting.builder()
                .direction(Sort.Direction.ASC)
                .property("id")
                .build();
        this.setSorting(sorting);

        Specification<C> specification = Specification.where(null);

        Specification<C> statusProductSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), ProductStatus.ACTIVE);

        Specification<C> statusCategorySpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("category").get("status"), CategoryStatus.ACTIVE);

        specification = specification.and(statusProductSpecification).and(statusCategorySpecification);

        if (StringUtils.hasText(this.filter.getName())) {

            specification = specification.or((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("category").get("name")),
                            STR."%\{this.filter.getName().toLowerCase()}%"));

            specification = specification.or((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                            STR."%\{this.filter.getName().toLowerCase()}%"));
        }

        return specification;
    }
}
