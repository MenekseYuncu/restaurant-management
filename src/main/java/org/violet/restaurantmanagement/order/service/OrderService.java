package org.violet.restaurantmanagement.order.service;

import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;

public interface OrderService {

    Order createOrder(OrderCreateCommand createCommand);

    void cancelOrder(String id);
}
