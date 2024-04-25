package org.violet.restaurantmanagement.common.model.mapper;

import org.springframework.data.jpa.domain.Specification;

public interface RmaSpecification {
    <C> Specification<C> toSpecification(final Class<C> clazz);
}
