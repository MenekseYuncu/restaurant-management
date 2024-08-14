package org.violet.restaurantmanagement.order.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.order.controller.request.OrderRequest;
import org.violet.restaurantmanagement.order.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
class OrderController {

    private final OrderService orderService;

    @PostMapping
    public BaseResponse<Void> createOrder(
            @Valid @RequestBody OrderRequest requests
    ) {
        return BaseResponse.SUCCESS;
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> cancelOrder(
            @PathVariable @Positive String id
    ) {
        orderService.cancelOrder(id);
        return BaseResponse.SUCCESS;
    }
}
