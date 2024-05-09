package org.violet.restaurantmanagement.product.service.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.violet.restaurantmanagement.common.model.mapper.RmaSpecification;
import org.violet.restaurantmanagement.common.pegable.Filtering;
import org.violet.restaurantmanagement.common.pegable.RmaPaginationCommand;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
public class CategoryListCommand extends RmaPaginationCommand implements RmaSpecification {

    private Filter filter;

    @Getter
    @Setter
    @Builder
    public static class Filter implements Filtering {

        private String name;

        private Set<CategoryStatus> statuses;

    }

    @Override
    public <C> Specification<C> toSpecification(Class<C> cClass) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (this.filter.name != null) {
            Specification<C> tempSpecification = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), this.filter.name));

            specification.and(tempSpecification);
        }

        if (this.filter.statuses != null) {
            Specification<C> tempSpecification = ((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("statuses"), this.filter.statuses));

            specification.and(tempSpecification);
        }

        return Specification.allOf();
    }
}
