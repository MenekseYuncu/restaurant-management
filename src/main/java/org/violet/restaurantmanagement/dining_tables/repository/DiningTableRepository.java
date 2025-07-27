package org.violet.restaurantmanagement.dining_tables.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;

import java.util.List;

@Repository
public interface DiningTableRepository extends JpaRepository<DiningTableEntity, Long>, JpaSpecificationExecutor<DiningTableEntity> {

    List<DiningTableEntity> findByMergeId(String mergeId);

    boolean existsByMergeId(String mergeId);

}
