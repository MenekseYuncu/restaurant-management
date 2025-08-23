package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.controller.response.OrderResponse;
import org.violet.restaurantmanagement.order.service.domain.Order;

@Mapper(componentModel = "spring", uses = {OrderItemToProductResponseMapper.class})
public interface OrderDomainToOrderResponseMapper extends BaseMapper<Order, OrderResponse> {

    OrderDomainToOrderResponseMapper INSTANCE = Mappers.getMapper(OrderDomainToOrderResponseMapper.class);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "items", target = "products")
    OrderResponse map(Order order);

}
