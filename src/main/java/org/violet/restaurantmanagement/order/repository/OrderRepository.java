package org.violet.restaurantmanagement.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String>, JpaSpecificationExecutor<OrderEntity> {

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items i WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(String id);

    List<OrderEntity> findAllByMergeIdOrderByCreatedAtDesc(String mergeId);

    @Query("SELECT o FROM OrderEntity o WHERE o.status = 'CANCELED' AND o.updatedAt <= :date")
    List<OrderEntity> findAllCanceledOrdersOlderThan7Days(LocalDateTime date);

}
