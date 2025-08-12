package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.Mapper;
import org.violet.restaurantmanagement.order.controller.request.OrderRemoveItemRequest;
import org.violet.restaurantmanagement.order.service.command.OrderRemoveItemCommand;

@Mapper(componentModel = "spring")
public interface OrderRemoveItemRequestToCommandMapper {

    OrderRemoveItemCommand map(OrderRemoveItemRequest source);

    OrderRemoveItemCommand.ProductItem map(OrderRemoveItemRequest.ProductItem source);
}
