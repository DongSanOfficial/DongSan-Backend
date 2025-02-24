//package com.dongsan.api.support.validation.annotation;
//
//import com.dongsan.api.support.validation.validator.NotEmptyCourseValidator;
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//
//import java.lang.annotation.*;
//
//@Documented
//@Constraint(validatedBy = NotEmptyCourseValidator.class)
//@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface NotEmptyWalkwayCourse {
//
//    String message() default "유효하지 않은 산책경로입니다.";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//
//}
