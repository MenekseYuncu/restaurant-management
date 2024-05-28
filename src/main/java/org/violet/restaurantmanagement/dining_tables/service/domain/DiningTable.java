package org.violet.restaurantmanagement.dining_tables.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class DiningTable {

    private Long id;
    @Builder.Default
    private String mergeId = UUID.randomUUID().toString();
    @Builder.Default
    private DiningTableStatus status = DiningTableStatus.VACANT;
    private Integer size;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
