package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.Mapper;
import org.violet.restaurantmanagement.order.controller.request.OrderUpdateRequest;
import org.violet.restaurantmanagement.order.service.command.OrderUpdateCommand;

@Mapper(componentModel = "spring")
public interface OrderUpdateRequestToCommandMapper {

    OrderUpdateCommand map(OrderUpdateRequest source);

    OrderUpdateCommand.ProductItem map(OrderUpdateRequest.ProductItem source);
}
