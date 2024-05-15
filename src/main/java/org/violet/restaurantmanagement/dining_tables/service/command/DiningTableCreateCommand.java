package org.violet.restaurantmanagement.dining_tables.service.command;

import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;

public record DiningTableCreateCommand(

        DiningTableStatus status,

        int size
) {
}
