package org.violet.restaurantmanagement.order.service.impl;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.violet.restaurantmanagement.RmaServiceTest;
import org.violet.restaurantmanagement.RmaTestContainer;
import org.violet.restaurantmanagement.order.repository.OrderRepository;

class OrderServiceImplTest extends RmaServiceTest implements RmaTestContainer {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;


}