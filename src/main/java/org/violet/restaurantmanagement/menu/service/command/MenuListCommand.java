package org.violet.restaurantmanagement.menu.service.command;

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

import java.util.Set;

@Getter
@SuperBuilder
public class MenuListCommand extends RmaPaginationCommand implements RmaSpecification {

    private MenuFilter filter;

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MenuFilter implements Filtering {

        private String name;

        private Set<ProductStatus> statuses;

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

        if (!CollectionUtils.isEmpty(this.filter.getStatuses())) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    root.get("status").in(this.filter.getStatuses()));
        }

        return specification;
    }
}
