package org.violet.restaurantmanagement.product.controller.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.violet.restaurantmanagement.common.pegable.Filtering;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFilter implements Filtering {

    private String name;

    public <C> Specification<C> toSpecification() {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(name)) {
                return criteriaBuilder.equal(root.get("name"), name);
            }
            return null;
        };
    }
}
