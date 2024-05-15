package org.violet.restaurantmanagement.dining_tables.service;

import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;

import java.util.List;

public interface DiningTableService {

    void createDiningTables(List<DiningTableCreateCommand> createCommand);
}
