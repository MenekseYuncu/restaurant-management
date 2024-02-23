package org.violet.restaurantmanagement.product.controller.response;

import java.time.LocalDateTime;

public record GeneralResponse(
        LocalDateTime time,
        String httpStatus,
        Boolean isSuccess
) {
}
