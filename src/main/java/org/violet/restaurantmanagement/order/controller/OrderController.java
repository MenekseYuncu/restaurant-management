package org.violet.restaurantmanagement.order.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.order.controller.mapper.*;
import org.violet.restaurantmanagement.order.controller.request.OrderCreateRequest;
import org.violet.restaurantmanagement.order.controller.request.OrderRemoveItemRequest;
import org.violet.restaurantmanagement.order.controller.request.OrderUpdateRequest;
import org.violet.restaurantmanagement.order.controller.response.OrderListResponse;
import org.violet.restaurantmanagement.order.controller.response.OrderResponse;
import org.violet.restaurantmanagement.order.service.OrderService;
import org.violet.restaurantmanagement.order.service.command.OrderCreateCommand;
import org.violet.restaurantmanagement.order.service.command.OrderRemoveItemCommand;
import org.violet.restaurantmanagement.order.service.command.OrderUpdateCommand;
import org.violet.restaurantmanagement.order.service.domain.Order;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
class OrderController {

    private final OrderCreateRequestToCommandMapper orderCreateRequestToCommandMapper;
    private final OrderUpdateRequestToCommandMapper orderUpdateRequestToCommandMapper;
    private final OrderRemoveItemRequestToCommandMapper orderRemoveItemRequestToCommandMapper;
    private final OrderDomainToOrderResponseMapper orderDomainToOrderResponseMapper;
    private final OrderDomainToOrderListResponseMapper orderListResponseMapper;

    private final OrderService orderService;

    @GetMapping("/{mergeId}")
    public BaseResponse<List<OrderListResponse>> getOrders(
            @PathVariable String mergeId
    ) {
        List<Order> orders = orderService.getOrdersByMergeId(mergeId);

        List<OrderListResponse> responseList = orders.stream()
                .map(orderListResponseMapper::map)
                .toList();

        return BaseResponse.successOf(responseList);
    }

    @PostMapping
    public BaseResponse<OrderResponse> createOrder(
            @Valid @RequestBody OrderCreateRequest requests
    ) {
        OrderCreateCommand createCommand = orderCreateRequestToCommandMapper.map(requests);
        Order order = orderService.createOrder(createCommand);
        OrderResponse response = orderDomainToOrderResponseMapper.map(order);
        return BaseResponse.successOf(response);
    }

    @PutMapping("/{id}")
    public BaseResponse<OrderResponse> updateOrder(
            @PathVariable String id,
            @Valid @RequestBody OrderUpdateRequest requests
    ) {
        OrderUpdateCommand updateCommand = orderUpdateRequestToCommandMapper.map(requests);
        Order order = orderService.updateOrder(id, updateCommand);
        OrderResponse response = orderDomainToOrderResponseMapper.map(order);
        return BaseResponse.successOf(response);
    }

    @PutMapping("/{id}/remove")
    public BaseResponse<OrderResponse> removeOrderItem(
            @PathVariable String id,
            @Valid @RequestBody OrderRemoveItemRequest requests
    ) {
        OrderRemoveItemCommand removeItemCommand = orderRemoveItemRequestToCommandMapper.map(requests);
        Order order = orderService.removeItemProductsFromOrder(id, removeItemCommand);
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

    @PatchMapping("/{id}/delivered")
    public BaseResponse<Void> changeStatusToDelivered(
            @PathVariable String id
    ) {
        orderService.changeOrderItemStatusToDelivered(id);
        return BaseResponse.SUCCESS;
    }

}
