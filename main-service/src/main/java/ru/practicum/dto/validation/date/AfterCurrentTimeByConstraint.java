package ru.practicum.dto.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.Instant;

import static ru.practicum.config.EWMServiceAppConfig.DATE_TIME_FORMATTER;

public class AfterCurrentTimeByConstraint implements ConstraintValidator<AfterCurrentTimeBy, String> {
    Integer hours = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        Instant instantValue = DATE_TIME_FORMATTER.parse(value, Instant::from);
        Instant instantNow = Instant.now();
        return instantValue.isAfter(instantNow.plus(Duration.ofHours(hours)));
    }

    @Override
    public void initialize(AfterCurrentTimeBy constraintAnnotation) {
        hours = constraintAnnotation.hours();
    }
}
