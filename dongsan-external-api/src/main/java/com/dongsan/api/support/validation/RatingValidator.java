package com.dongsan.api.support.validation;

import org.springframework.stereotype.Component;

import com.dongsan.core.domains.review.Rating;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
