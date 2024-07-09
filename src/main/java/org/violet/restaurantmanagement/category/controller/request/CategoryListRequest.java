package org.violet.restaurantmanagement.category.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.violet.restaurantmanagement.category.model.enums.CategoryStatus;
import org.violet.restaurantmanagement.common.controller.requset.RmaPaginationRequest;
import org.violet.restaurantmanagement.common.model.Filtering;

import java.util.Set;

@Getter
@SuperBuilder
@NoArgsConstructor
public class CategoryListRequest extends RmaPaginationRequest {

    private CategoryFilter filter;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryFilter implements Filtering {

        private String name;

        private Set<CategoryStatus> statuses;

    }


    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("name");
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
