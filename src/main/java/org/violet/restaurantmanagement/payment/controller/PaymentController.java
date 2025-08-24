package org.violet.restaurantmanagement.payment.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.violet.restaurantmanagement.common.controller.response.BaseResponse;
import org.violet.restaurantmanagement.payment.controller.mapper.ProcessPaymentToProcessRequestMapper;
import org.violet.restaurantmanagement.payment.controller.request.ProcessPaymentRequest;
import org.violet.restaurantmanagement.payment.service.PaymentService;
import org.violet.restaurantmanagement.payment.service.command.ProcessPaymentCommand;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final ProcessPaymentToProcessRequestMapper processPaymentToProcessRequestMapper;
    private final PaymentService paymentService;

    @PostMapping("/{orderId}")
    public BaseResponse<Void> processPayment(
            @PathVariable String orderId,
            @Valid @RequestBody ProcessPaymentRequest requests
    ) {
        ProcessPaymentCommand paymentCommand = processPaymentToProcessRequestMapper.map(requests);
        paymentService.processPayment(orderId, paymentCommand);
        return BaseResponse.SUCCESS;
    }
}