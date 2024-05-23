package org.violet.restaurantmanagement.category.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Category {

    private Long id;
    private String name;
    @Builder.Default
    private CategoryStatus status = CategoryStatus.ACTIVE;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void delete() {
        this.status = CategoryStatus.DELETED;
    }
}
