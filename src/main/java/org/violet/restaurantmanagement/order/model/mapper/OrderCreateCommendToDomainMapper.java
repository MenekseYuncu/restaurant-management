package org.violet.restaurantmanagement.order.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;

@Mapper(componentModel = "spring")
public interface OrderCreateCommendToDomainMapper extends BaseMapper<OrderCreateCommand, Order> {

    OrderCreateCommendToDomainMapper INSTANCE = Mappers.getMapper(OrderCreateCommendToDomainMapper.class);

}
