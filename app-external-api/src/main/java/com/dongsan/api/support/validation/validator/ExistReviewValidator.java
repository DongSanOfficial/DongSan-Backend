//package com.dongsan.api.support.validation.validator;
//
//import com.dongsan.api.support.validation.annotation.ExistReview;
//import com.dongsan.core.domains.review.ReviewReader;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ExistReviewValidator implements ConstraintValidator<ExistReview, Long> {
//    private final ReviewReader reviewReader;
//
//    @Override
//    public boolean isValid(Long value, ConstraintValidatorContext context) {
//        boolean isValid = false;
//        if(value != null){
//            isValid = reviewReader.existsByReviewId(value);
//        }
//
//        if (!isValid) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate(
//                            ReviewErrorCode.REVIEW_NOT_FOUND.toString())
//                    .addConstraintViolation();
//        }
//
//        return isValid;
//    }
//}
