//package com.dongsan.api.support.validation.validator;
//
//import com.dongsan.api.support.validation.annotation.ExistBookmark;
//import com.dongsan.core.domains.bookmark.BookmarkReader;
//import com.dongsan.common.error.code.BookmarkErrorCode;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ExistBookmarkValidator implements ConstraintValidator<ExistBookmark, Long> {
//    private final BookmarkReader bookmarkReader;
//    @Override
//    public boolean isValid(Long value, ConstraintValidatorContext context) {
//        boolean isValid = false;
//        if(value != null){
//            isValid = bookmarkReader.existsById(value);
//        }
//
//        if(!isValid){
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate(
//                            BookmarkErrorCode.BOOKMARK_NOT_EXIST.toString())
//                    .addConstraintViolation();
//        }
//
//        return isValid;
//    }
//}
