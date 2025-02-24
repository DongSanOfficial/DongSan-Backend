//package com.dongsan.api.support.validation.annotation;
//
//import com.dongsan.api.support.validation.validator.ExistReviewValidator;
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Documented
//@Constraint(validatedBy = ExistReviewValidator.class)
//@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ExistReview {
//    String message() default "해당 리뷰 ID가 존재하지 않습니다.";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//}
