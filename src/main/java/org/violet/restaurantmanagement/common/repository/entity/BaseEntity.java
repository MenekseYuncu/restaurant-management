package org.violet.restaurantmanagement.common.repository.entity;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public abstract class BaseEntity {

    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;
}
