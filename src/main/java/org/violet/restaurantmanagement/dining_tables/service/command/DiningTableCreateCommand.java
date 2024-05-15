package org.violet.restaurantmanagement.dining_tables.service.command;

public record DiningTableCreateCommand(

        int numberOfTables,

        int size
) {
}
