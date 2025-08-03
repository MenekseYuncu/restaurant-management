package org.violet.restaurantmanagement.dining_tables.service.command;

import lombok.Builder;

@Builder
public record DiningTableCreateCommand(

        Integer count,

        Integer size
) {
}
