package org.violet.restaurantmanagement.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.violet.restaurantmanagement.common.model.enums.RmaCurrency;
import org.violet.restaurantmanagement.parameter.model.ParameterEntity;
import org.violet.restaurantmanagement.parameter.repository.ParameterRepository;

import java.util.Optional;

@Configuration
public class CurrencyConfiguration {

    @Bean
    String currency(ParameterRepository parameterRepository) {
        Optional<ParameterEntity> parameterEntity = parameterRepository.findByName(String.valueOf(RmaCurrency.CURRENCY));
        return parameterEntity.orElseThrow().getDefinition();
    }

}
