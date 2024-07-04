package org.violet.restaurantmanagement.dining_tables.service.command;

import java.util.List;

public record DiningTableMergeCommand(

        List<Long> tableIds
) {
}
