package org.violet.restaurantmanagement.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.product.repository.entity.ParameterEntity;

import java.util.Optional;

@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity, Long>, JpaSpecificationExecutor<ParameterEntity> {

    Optional<ParameterEntity> findByName(String name);
}
