package org.violet.restaurantmanagement.product.controller.util;

import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.violet.restaurantmanagement.common.pegable.Filtering;
import org.violet.restaurantmanagement.product.model.enums.CategoryStatus;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryFilter implements Filtering {

    private String name;

    private Set<CategoryStatus> statuses;

    public <C> Specification<C> toSpecification() {
        return (root, query, criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            if (StringUtils.hasText(name)) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like
                        (criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (statuses != null && !statuses.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, root.get("status").in(statuses));
            }
            return predicate;
        };
    }
}
