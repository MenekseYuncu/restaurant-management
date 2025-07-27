package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.controller.response.OrderResponse;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.product.repository.ProductRepository;

@Mapper(componentModel = "spring")
public abstract class OrderItemToProductResponseMapper implements BaseMapper<OrderResponse.OrderProductResponse, OrderItem> {

    protected ProductRepository productRepository;

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "price", target = "price")
    public abstract OrderResponse.OrderProductResponse map(OrderItem item);

    @AfterMapping
    protected void afterMapping(OrderItem item, @MappingTarget OrderResponse.OrderProductResponse response) {
        productRepository.findById(item.getProductId())
                .ifPresent(product -> response.setProductName(product.getName()));
    }
}
