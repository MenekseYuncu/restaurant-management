package org.violet.restaurantmanagement.common.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@Builder
public class Sorting {

    private String property;

    private Sort.Direction direction;

    public static Sorting toSort(final Sort sort) {
        return sort.stream()
                .findFirst()
                .map(order -> Sorting.builder()
                        .property(order.getProperty())
                        .direction(order.getDirection())
                        .build())
                .orElse(null);
    }
}
