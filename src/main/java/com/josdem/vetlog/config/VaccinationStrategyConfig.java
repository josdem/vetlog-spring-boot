package com.josdem.vetlog.config;

import com.josdem.vetlog.enums.PetType;
import com.josdem.vetlog.strategy.vaccination.VaccinationStrategy;
import com.josdem.vetlog.strategy.vaccination.impl.CatVaccinationStrategy;
import com.josdem.vetlog.strategy.vaccination.impl.DogVaccinationStrategy;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VaccinationStrategyConfig {
    @Bean
    public Map<PetType, VaccinationStrategy> vaccinationStrategies(
            DogVaccinationStrategy dogVaccinationStrategy, CatVaccinationStrategy catVaccinationStrategy) {
        Map<PetType, VaccinationStrategy> strategies = new HashMap<>();
        strategies.put(PetType.DOG, dogVaccinationStrategy);
        strategies.put(PetType.CAT, catVaccinationStrategy);
        return strategies;
    }
}
