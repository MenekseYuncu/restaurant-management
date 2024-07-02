package org.violet.restaurantmanagement.parameter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.parameter.model.ParameterEntity;

import java.util.Optional;

@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity, Integer> {

    Optional<ParameterEntity> findByName(String name);
}
