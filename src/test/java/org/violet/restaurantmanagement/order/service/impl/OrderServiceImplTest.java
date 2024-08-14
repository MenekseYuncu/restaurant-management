package org.violet.restaurantmanagement.order.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.order.repository.OrderRepository;

import java.util.Optional;
import java.util.UUID;

class OrderServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    void givenInvalidMergeId_whenPlacingAnOrder_thenThrowException() {
        // Given
        String mergeId = UUID.randomUUID().toString();

        // When
        Mockito.when(orderRepository.findByMergeId(mergeId))
                .thenReturn(Optional.empty());

        // Verify
        Mockito.verify(orderRepository, Mockito.never())
                .save(ArgumentMatchers.any());
    }


}