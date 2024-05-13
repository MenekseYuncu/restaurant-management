package org.violet.restaurantmanagement.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.category.repository.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> , JpaSpecificationExecutor<CategoryEntity> {

    boolean existsByName(String name);
}
