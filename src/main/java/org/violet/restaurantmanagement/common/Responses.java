package org.violet.restaurantmanagement.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Responses<T> {

    @Builder.Default
    private LocalDateTime time = LocalDateTime.now();
    private HttpStatus httpStatus;
    private Boolean isSuccess;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T response;

    public static final Responses<Void> SUCCESS = Responses.<Void>builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true).build();


    public static <T> Responses<T> successOf(final T response) {
        return Responses.<T>builder()
                .httpStatus(HttpStatus.OK)
                .isSuccess(true)
                .response(response).build();
    }
}
