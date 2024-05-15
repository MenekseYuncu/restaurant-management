package org.violet.restaurantmanagement.dining_tables.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DiningTable {

    private Long id;
    private String mergeId;
    private DiningTableStatus status;
    private int size;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void tableStatus() {
        this.status = DiningTableStatus.VACANT;
    }

    public void merge() {
        this.mergeId = UUID.randomUUID().toString();
    }
}
