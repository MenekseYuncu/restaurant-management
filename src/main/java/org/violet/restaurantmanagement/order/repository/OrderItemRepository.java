package org.violet.restaurantmanagement.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, String>, JpaSpecificationExecutor<OrderItemEntity> {

    List<OrderItemEntity> findOrderItemEntitiesByOrderId(String orderId);
}
