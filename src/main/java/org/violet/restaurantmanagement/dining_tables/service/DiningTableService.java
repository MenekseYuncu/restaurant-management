package org.violet.restaurantmanagement.dining_tables.service;

import org.violet.restaurantmanagement.common.model.RmaPage;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableCreateCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableListCommand;
import org.violet.restaurantmanagement.dining_tables.service.command.DiningTableUpdateCommand;
import org.violet.restaurantmanagement.dining_tables.service.domain.DiningTable;

public interface DiningTableService {

    RmaPage<DiningTable> getAllDiningTables(DiningTableListCommand diningTableListCommand);

    DiningTable getDiningTableById(Long id);

    void createDiningTables(DiningTableCreateCommand createCommands);

    void updateDiningTable(Long id, DiningTableUpdateCommand updateCommand);

    void changeStatusToVacant(Long id);

    void changeStatusToOccupied(Long id);

    void deleteDiningTable(Long id);
}
