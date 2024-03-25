package org.violet.restaurantmanagement.common.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class BaseResponse<T> {

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
    private HttpStatus httpStatus;
    private Boolean isSuccess;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;

    public static final BaseResponse<Void> SUCCESS = BaseResponse.<Void>builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true).build();


    public static <T> BaseResponse<T> successOf(final T response) {
        return BaseResponse.<T>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(true)
                .response(response).build();
    }
}
