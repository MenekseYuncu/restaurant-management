package org.violet.restaurantmanagement.dining_tables.service.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.common.model.mapper.RmaSpecification;
import org.violet.restaurantmanagement.common.service.command.RmaPaginationCommand;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;

import java.util.Set;

@Getter
@SuperBuilder
public class DiningTableListCommand extends RmaPaginationCommand implements RmaSpecification {

    private DiningTableFilter filter;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class DiningTableFilter implements Filtering {

        private Integer size;

        private Set<DiningTableStatus> statuses;

    }

    @Override
    public <C> Specification<C> toSpecification(Class<C> clazz) {

        if (this.filter == null) {
            return Specification.allOf();
        }

        Specification<C> specification = Specification.where(null);

        if (this.filter.getSize() != null) {
            Specification<C> sizeSpecification = (root, _, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("size"), this.filter.getSize());
            specification = specification.and(sizeSpecification);
        }

        if (!CollectionUtils.isEmpty(this.filter.getStatuses())) {
            Specification<C> statusSpecification = (root, _, _) ->
                    root.get("status").in(this.filter.getStatuses());
            specification = specification.and(statusSpecification);
        }

        return specification;
    }
}
