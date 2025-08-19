package org.violet.restaurantmanagement.schedule;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.violet.restaurantmanagement.order.service.OrderService;


@ExtendWith(MockitoExtension.class)
class OrderScheduleTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderSchedule orderSchedule;

    @Test
    void deleteAllCanceledOrders_Success() {
        // When
        orderSchedule.deleteAllCanceledOrders();

        // Then
        Mockito.verify(orderService, Mockito.times(1))
                .deleteCanceledOrders();
    }

    @Test
    void deleteAllCanceledOrders_WhenExceptionThrown_ShouldLogError() {
        // Given
        RuntimeException exception = new RuntimeException("Database error");
        Mockito.doThrow(exception).when(orderService).deleteCanceledOrders();

        // When
        orderSchedule.deleteAllCanceledOrders();

        // Then
        Mockito.verify(orderService, Mockito.times(1))
                .deleteCanceledOrders();
    }
}