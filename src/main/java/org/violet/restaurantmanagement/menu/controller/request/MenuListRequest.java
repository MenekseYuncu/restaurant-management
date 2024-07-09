package org.violet.restaurantmanagement.menu.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.violet.restaurantmanagement.common.controller.requset.RmaPaginationRequest;
import org.violet.restaurantmanagement.common.model.Filtering;

import java.util.Set;

@Getter
@SuperBuilder
@NoArgsConstructor
public class MenuListRequest extends RmaPaginationRequest {

    private MenuFilter filter;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuFilter implements Filtering {

        private String name;

    }


    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("categoryId", "id");
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
