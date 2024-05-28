package org.violet.restaurantmanagement.dining_tables.service;

import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

public interface DiningTableService {

    DiningTable getDiningTableById(Long id);
    void createDiningTables(DiningTableCreateCommand createCommands);

    void updateDiningTable(Long id, DiningTableUpdateCommand updateCommand);

    void deleteDiningTable(Long id);
}
