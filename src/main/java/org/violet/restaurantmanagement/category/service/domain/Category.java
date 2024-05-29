package org.violet.restaurantmanagement.category.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    private CategoryStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void isActive() {
        this.status = CategoryStatus.ACTIVE;
    }
}
