package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;

@Mapper(componentModel = "spring")
public interface OrderCreateRequestToCommandMapper extends BaseMapper<OrderCreateRequest, OrderCreateCommand> {

    OrderCreateRequestToCommandMapper INSTANCE = Mappers.getMapper(OrderCreateRequestToCommandMapper.class);
}
