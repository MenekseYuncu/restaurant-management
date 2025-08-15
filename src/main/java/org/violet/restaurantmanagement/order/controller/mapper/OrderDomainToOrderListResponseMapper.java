package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.violet.restaurantmanagement.order.controller.response.OrderListResponse;
import org.violet.restaurantmanagement.order.service.domain.Order;

@Mapper(componentModel = "spring", uses = {OrderItemToProductListResponseMapper.class})
public interface OrderDomainToOrderListResponseMapper {

    OrderDomainToOrderListResponseMapper INSTANCE = Mappers.getMapper(OrderDomainToOrderListResponseMapper.class);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "totalAmount", target = "totalAmount")
    @Mapping(source = "items", target = "products")
    OrderListResponse map(Order order);

}