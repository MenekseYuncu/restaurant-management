package org.violet.restaurantmanagement.order.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;

@Service
@RequiredArgsConstructor
class OrderServiceImpl implements OrderService {

    @Override
    public Order createOrder(OrderCreateCommand createCommand) {
        return null;
    }

    @Override
    public void cancelOrder(String id) {

    }
}
