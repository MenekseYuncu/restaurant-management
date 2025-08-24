package org.violet.restaurantmanagement.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.violet.restaurantmanagement.payment.repository.entity.PaymentEntity;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, String>, JpaSpecificationExecutor<PaymentEntity> {


    Optional<PaymentEntity> findByOrderId(String orderId);
}