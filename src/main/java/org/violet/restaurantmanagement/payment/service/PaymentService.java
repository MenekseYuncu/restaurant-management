package org.violet.restaurantmanagement.payment.service;

import org.violet.restaurantmanagement.payment.service.command.ProcessPaymentCommand;

public interface PaymentService {

    void processPayment(String orderId, ProcessPaymentCommand command);
}