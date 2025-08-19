package org.violet.restaurantmanagement.order.service;

import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.command.OrderRemoveItemCommand;
import org.violet.restaurantmanagement.order.service.command.OrderUpdateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;

import java.util.List;

public interface OrderService {

    List<Order> getOrdersByMergeId(String mergeId);

    Order createOrder(OrderCreateCommand createCommand);

    Order updateOrder(String id, OrderUpdateCommand updateCommand);

    Order removeItemProductsFromOrder(String id, OrderRemoveItemCommand removeItemCommand);

    void cancelOrder(String id);

    void deleteCanceledOrders();

    void changeOrderItemStatusToDelivered(String id);

    void changeOrderItemStatusToReady(String id);

    void changeOrderItemStatusToCancelled(String id);

}
