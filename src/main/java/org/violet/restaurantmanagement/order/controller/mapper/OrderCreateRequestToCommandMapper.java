package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.Mapper;
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;

@Mapper(componentModel = "spring")
public interface OrderCreateRequestToCommandMapper {

    OrderCreateCommand map(OrderCreateRequest source);

    OrderCreateCommand.ProductItem map(OrderCreateRequest.ProductItem source);
}
