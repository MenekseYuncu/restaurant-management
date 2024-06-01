package org.violet.restaurantmanagement.common.model.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseMapper<S, T> {

    T map(S source);

    List<T> map(List<S> source);

    void update(@MappingTarget T target, S source);
}
