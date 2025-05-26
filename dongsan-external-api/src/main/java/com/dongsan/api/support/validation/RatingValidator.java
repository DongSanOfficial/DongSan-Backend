package com.dongsan.api.support.validation;

import com.dongsan.domain.domains.review.domain.Rating;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RatingValidator implements ConstraintValidator<ValidRating, Integer> {
    @Override
    public boolean isValid(Integer inputRating, ConstraintValidatorContext context) {
        for (Rating rating : Rating.values()) {
            if (rating.getNum()
                    .equals(inputRating))
                return true;
        }

        return false;
    }
}
