package org.violet.restaurantmanagement.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.order.exceptions.MergeIdNotFoundException;
import org.violet.restaurantmanagement.order.repository.OrderRepository;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;

@Service
@RequiredArgsConstructor
class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(OrderCreateCommand createCommand) {

        OrderEntity orderEntity = orderRepository.findByMergeId(createCommand.mergeId())
                .orElseThrow(MergeIdNotFoundException::new);

        return null;
    }
}
