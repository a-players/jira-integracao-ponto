package com.mindproapps.jira.integracaoponto.validator;

import com.mindproapps.jira.integracaoponto.exception.InvalidDateIntervalException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PeriodValidator {

    public void validateDateInterval(LocalDate initialDate, LocalDate finalDate) {
        if(initialDate == null) {
            initialDate = LocalDate.now();
        }
        if(finalDate == null) {
            finalDate = LocalDate.now();
        }
        if(finalDate.isBefore(initialDate)) {
            throw new InvalidDateIntervalException("Start Date cannot be higher than End Date");
        }
    }

}
