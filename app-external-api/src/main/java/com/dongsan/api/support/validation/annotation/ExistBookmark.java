//package com.dongsan.api.support.validation.annotation;
//
//import com.dongsan.api.support.validation.validator.ExistBookmarkValidator;
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Documented
//@Constraint(validatedBy = ExistBookmarkValidator.class)
//@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ExistBookmark {
//    String message() default "해당 북마크 ID가 존재하지 않습니다.";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//}