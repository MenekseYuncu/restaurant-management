package org.violet.restaurantmanagement.payment.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;
import org.violet.restaurantmanagement.dining_tables.repository.DiningTableRepository;
import org.violet.restaurantmanagement.dining_tables.repository.entity.DiningTableEntity;
import org.violet.restaurantmanagement.order.exceptions.OrderItemNotFoundException;
import org.violet.restaurantmanagement.order.exceptions.OrderNotFoundException;
import org.violet.restaurantmanagement.order.repository.OrderItemRepository;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.payment.exceptions.OrderItemDoesNotBelongToOrderException;
import org.violet.restaurantmanagement.payment.exceptions.PaymentAlreadyCompletedException;
import org.violet.restaurantmanagement.payment.repository.PaymentRepository;
import org.violet.restaurantmanagement.payment.repository.entity.PaymentEntity;
import org.violet.restaurantmanagement.payment.service.PaymentService;
import org.violet.restaurantmanagement.payment.service.command.ProcessPaymentCommand;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DiningTableRepository diningTableRepository;

    @Override
    @Transactional
    public void processPayment(final String orderId, final ProcessPaymentCommand command) {
        final OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        final PaymentEntity payment = getOrCreateActivePayment(order);

        for (ProcessPaymentCommand.OrderItemDto dto : command.orderItems()) {
            final OrderItemEntity orderItem = loadAndValidateItem(orderId, dto.getOrderItemId());
            payment.pay(orderItem);
        }

        payment.completeIfFullyPaid(order);

        paymentRepository.save(payment);
        orderRepository.save(order);

        if (payment.isCompleted()) {
            final List<DiningTableEntity> tables = diningTableRepository.findByMergeId(order.getMergeId());
            tables.forEach(t -> t.setStatus(DiningTableStatus.VACANT));
            diningTableRepository.saveAll(tables);
        }
    }

    private PaymentEntity getOrCreateActivePayment(final OrderEntity order) {
        PaymentEntity payment = paymentRepository.findByOrderId(order.getId())
                .orElseThrow(OrderNotFoundException::new);

        if (payment == null) {
            payment = PaymentEntity.buildPayment(order);
            payment.setTotalAmount(BigDecimal.ZERO);
            return payment;
        }
        if (payment.isCompleted()) {
            throw new PaymentAlreadyCompletedException(order.getId());
        }
        return payment;
    }

    private OrderItemEntity loadAndValidateItem(final String orderId, final String orderItemId) {
        final OrderItemEntity item = orderItemRepository.findById(orderItemId)
                .orElseThrow(OrderItemNotFoundException::new);


        if (!orderId.equals(item.getOrderId())) {
            throw new OrderItemDoesNotBelongToOrderException(orderItemId, orderId);
        }
        return item;
    }
}