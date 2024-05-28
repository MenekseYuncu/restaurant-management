package org.violet.restaurantmanagement.dining_tables.controller.response;

import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;

import java.time.LocalDateTime;

public record DiningTableResponse(

        Long id,
        String mergeId,
        Integer size,
        DiningTableStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
}
