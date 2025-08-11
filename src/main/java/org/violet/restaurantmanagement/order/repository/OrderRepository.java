package org.violet.restaurantmanagement.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String>, JpaSpecificationExecutor<OrderEntity> {

    @Query("SELECT o FROM OrderEntity o LEFT JOIN FETCH o.items i WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(String id);

}
