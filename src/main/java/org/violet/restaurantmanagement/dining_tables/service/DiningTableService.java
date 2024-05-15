package org.violet.restaurantmanagement.dining_tables.service;

import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;

public interface DiningTableService {

    void createDiningTables(DiningTableCreateCommand createCommands);

    void updateDiningTable(Long id, DiningTableUpdateCommand updateCommand);
}
