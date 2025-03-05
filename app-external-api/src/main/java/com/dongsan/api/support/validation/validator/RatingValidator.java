package com.dongsan.api.support.validation.validator;

import com.dongsan.api.support.validation.annotation.ValidRating;
import com.dongsan.core.domains.review.Rating;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RatingValidator implements ConstraintValidator<ValidRating, Integer> {
    @Override
    public boolean isValid(Integer inputRating, ConstraintValidatorContext context) {
        for(Rating rating : Rating.values()) {
            if (rating.getNum().equals(inputRating))
                return true;
        }

        return false;
    }
}
