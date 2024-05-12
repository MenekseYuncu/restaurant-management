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
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

import java.util.Set;

import static java.lang.StringTemplate.STR;

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

        if (StringUtils.hasText(this.filter.getName())) {
            Specification<C> tempSpecification = (root, _, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), STR."%\{filter.getName().toLowerCase()}%");
            specification = specification.and(tempSpecification);
        }

        if (!CollectionUtils.isEmpty(this.filter.getStatuses())) {
            Specification<C> statusSpecification = (root, _, _) ->
                    root.get("status").in(this.filter.getStatuses());
            specification = specification.and(statusSpecification);
        }

        return specification;
    }
}
