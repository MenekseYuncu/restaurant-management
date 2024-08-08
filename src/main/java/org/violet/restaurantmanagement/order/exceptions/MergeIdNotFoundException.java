package org.violet.restaurantmanagement.order.exceptions;

import org.violet.restaurantmanagement.common.exception.RmaNotFoundException;

import java.io.Serial;

public class MergeIdNotFoundException extends RmaNotFoundException {


    @Serial
    private static final long serialVersionUID = 875482153831356777L;

    public MergeIdNotFoundException() {
        super("Merge id not found");
    }
}
