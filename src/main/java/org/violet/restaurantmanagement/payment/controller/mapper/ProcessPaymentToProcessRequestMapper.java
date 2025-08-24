package org.violet.restaurantmanagement.payment.controller.mapper;

import org.mapstruct.Mapper;
import org.violet.restaurantmanagement.payment.controller.request.ProcessPaymentRequest;
import org.violet.restaurantmanagement.payment.service.command.ProcessPaymentCommand;

@Mapper(componentModel = "spring")
public interface ProcessPaymentToProcessRequestMapper {

    ProcessPaymentCommand map(ProcessPaymentRequest source);

    ProcessPaymentCommand.OrderItemDto map(ProcessPaymentRequest.OrderItemDto source);

}