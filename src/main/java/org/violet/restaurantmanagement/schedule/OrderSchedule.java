package org.violet.restaurantmanagement.schedule;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.violet.restaurantmanagement.order.service.OrderService;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSchedule {

    private final OrderService orderService;

    @Scheduled(cron = "0 0 * * * *", zone = "Europe/Istanbul")
    public void deleteAllCanceledOrders() {
        try {
            log.info("Cron job started: Deleting canceled orders older than 7 days.");

            orderService.deleteCanceledOrders();

            log.info("Cron job completed: Successfully deleted canceled orders older than 7 days.");
        } catch (Exception e) {
            log.error("Error occurred while deleting canceled orders: ", e);
        }
    }
}
