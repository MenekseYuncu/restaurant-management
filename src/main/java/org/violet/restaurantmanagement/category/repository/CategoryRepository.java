package org.violet.restaurantmanagement.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> , JpaSpecificationExecutor<CategoryEntity> {

    Optional<CategoryEntity> findByName(String name);

    boolean existsByIdAndStatusIsNot(Long id, CategoryStatus status);
}
