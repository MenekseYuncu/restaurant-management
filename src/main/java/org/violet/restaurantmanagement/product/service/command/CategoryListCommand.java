package org.violet.restaurantmanagement.product.service.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.common.model.mapper.RmaSpecification;
import org.violet.restaurantmanagement.common.service.command.RmaPaginationCommand;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

import java.util.Set;

@Getter
@SuperBuilder
public class CategoryListCommand extends RmaPaginationCommand implements RmaSpecification {

    private CategoryFilter filter;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class CategoryFilter implements Filtering {

        private String name;

        private Set<CategoryStatus> statuses;

    }

    @Override
    public <C> Specification<C> toSpecification(Class<C> cClass) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (this.filter.getName() != null) {
            Specification<C> tempSpecification = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), this.filter.getName()));

            specification.and(tempSpecification);
        }

        if (this.filter.getStatuses() != null) {
            Specification<C> tempSpecification = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("statuses"), this.filter.getStatuses()));

            specification.and(tempSpecification);
        }

        return Specification.allOf();
    }
}
