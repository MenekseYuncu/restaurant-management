package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.controller.request.OrderRequest;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;

@Mapper
public interface OrderCreateRequestToCommandMapper extends BaseMapper<OrderRequest, OrderCreateCommand> {

    OrderCreateRequestToCommandMapper INSTANCE = Mappers.getMapper(OrderCreateRequestToCommandMapper.class);
}
