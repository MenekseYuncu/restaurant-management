package org.violet.restaurantmanagement.common.pegable;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@Builder
public class Sorting {

    @NotNull
    public String orderBy;

    @NotNull
    public Sort.Direction order;

    public static List<Sorting> of(final Sort sort) {
        return sort.stream()
                .map(order -> Sorting.builder()
                        .orderBy(order.getProperty())
                        .order(order.getDirection())
                        .build())
                .toList();
    }
}
