package org.violet.restaurantmanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.violet.restaurantmanagement.common.model.enums.RmaCurrency;
import org.violet.restaurantmanagement.common.model.enums.RmaParameter;
import org.violet.restaurantmanagement.parameter.model.ParameterEntity;
import org.violet.restaurantmanagement.parameter.repository.ParameterRepository;

import java.util.Optional;

@Configuration
public class CurrencyConfiguration {

    @Bean
    RmaCurrency currency(ParameterRepository parameterRepository) {
        Optional<ParameterEntity> parameterEntity = parameterRepository.findByName(RmaParameter.CURRENCY.name());
        return RmaCurrency.valueOf(parameterEntity.orElseThrow().getDefinition());
    }
}
