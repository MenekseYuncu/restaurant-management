package org.violet.restaurantmanagement.dining_tables.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.violet.restaurantmanagement.common.controller.requset.RmaPaginationRequest;
import org.violet.restaurantmanagement.common.model.Filtering;
import org.violet.restaurantmanagement.dining_tables.model.enums.DiningTableStatus;

import java.util.Set;

@Getter
@SuperBuilder
@NoArgsConstructor
public class DiningTableListRequest extends RmaPaginationRequest {

    private DiningTableFilter filter;

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiningTableFilter implements Filtering {

        private Integer size;

        private Set<DiningTableStatus> statuses;

    }


    @Override
    public boolean isOrderPropertyAccepted() {
        final Set<String> acceptedFilterFields = Set.of("id");
        return this.isPropertyAccepted(acceptedFilterFields);
    }
}
