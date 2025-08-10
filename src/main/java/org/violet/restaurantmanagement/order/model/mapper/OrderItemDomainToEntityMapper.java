package org.violet.restaurantmanagement.order.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.repository.entity.OrderItemEntity;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemDomainToEntityMapper extends BaseMapper<OrderItem, OrderItemEntity> {

    OrderItemDomainToEntityMapper INSTANCE = Mappers.getMapper(OrderItemDomainToEntityMapper.class);

}
