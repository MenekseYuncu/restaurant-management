package org.violet.restaurantmanagement.order.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.order.controller.mapper.OrderCreateRequestToCommandMapper;
import org.violet.restaurantmanagement.order.controller.mapper.OrderDomainToOrderResponseMapper;
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.controller.response.OrderResponse;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
class OrderController {

    private static final OrderCreateRequestToCommandMapper orderCreateRequestToCommandMapper = OrderCreateRequestToCommandMapper.INSTANCE;
    private final OrderDomainToOrderResponseMapper orderDomainToOrderResponseMapper;
    private final OrderService orderService;

    @PostMapping
    public BaseResponse<OrderResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest requests
    ) {
        OrderCreateCommand createCommand = orderCreateRequestToCommandMapper.map(requests);
        Order order = orderService.createOrder(createCommand);
        OrderResponse response = orderDomainToOrderResponseMapper.map(order);
        return BaseResponse.successOf(response);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> cancelOrder(
            @PathVariable String id
    ) {
        orderService.cancelOrder(id);
        return BaseResponse.SUCCESS;
    }
}
