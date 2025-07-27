package org.violet.restaurantmanagement.order.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.repository.entity.OrderEntity;
import org.violet.restaurantmanagement.order.service.domain.Order;

@Mapper
public interface OrderEntityToDomainMapper extends BaseMapper<OrderEntity, Order> {

    OrderEntityToDomainMapper INSTANCE = Mappers.getMapper(OrderEntityToDomainMapper.class);

    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Order map(OrderEntity entity);

}
