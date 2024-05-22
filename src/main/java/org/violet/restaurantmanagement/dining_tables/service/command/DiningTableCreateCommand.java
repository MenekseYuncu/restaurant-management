package org.violet.restaurantmanagement.dining_tables.service.command;

public record DiningTableCreateCommand(

        Integer numberOfTables,

        Integer size
) {
}
