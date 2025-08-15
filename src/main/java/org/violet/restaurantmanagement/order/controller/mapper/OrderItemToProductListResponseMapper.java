package org.violet.restaurantmanagement.order.controller.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.violet.restaurantmanagement.common.model.mapper.BaseMapper;
import org.violet.restaurantmanagement.order.controller.response.OrderListResponse;
import org.violet.restaurantmanagement.order.service.domain.OrderItem;
import org.violet.restaurantmanagement.product.repository.ProductRepository;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class OrderItemToProductListResponseMapper implements BaseMapper<OrderListResponse.OrderProductResponse, OrderItem> {

    @Autowired
    private ProductRepository productRepository;

    @Mapping(source = "productId", target = "productId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "price", target = "price")
    public abstract OrderListResponse.OrderProductResponse map(OrderItem item);

    @AfterMapping
    protected void afterMapping(OrderItem item, @MappingTarget OrderListResponse.OrderProductResponse response) {
        productRepository.findById(item.getProductId())
                .ifPresent(product -> response.setProductName(product.getName()));
    }
}