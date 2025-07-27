package org.violet.restaurantmanagement.order.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.service.domain.Order;

@Mapper
public interface OrderDomainToEntityMapper extends BaseMapper<Order, OrderEntity> {

    OrderDomainToEntityMapper INSTANCE = Mappers.getMapper(OrderDomainToEntityMapper.class);

}
