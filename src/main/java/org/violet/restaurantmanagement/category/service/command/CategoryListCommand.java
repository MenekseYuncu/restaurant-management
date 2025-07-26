package org.violet.restaurantmanagement.category.service.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.common.model.mapper.RmaSpecification;
import org.violet.restaurantmanagement.common.service.command.RmaPaginationCommand;

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
    @SuppressWarnings("This method is unused by the application directly but Spring is using it in the background.")
    public <C> Specification<C> toSpecification(Class<C> cClass) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (StringUtils.hasText(this.filter.getName())) {
            Specification<C> tempSpecification = (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%");
            specification = specification.and(tempSpecification);
        }

        if (!CollectionUtils.isEmpty(this.filter.getStatuses())) {
            Specification<C> statusSpecification = (root, query, criteriaBuilder) ->
                    root.get("status").in(this.filter.getStatuses());
            specification = specification.and(statusSpecification);
        }

        return specification;
    }
}
