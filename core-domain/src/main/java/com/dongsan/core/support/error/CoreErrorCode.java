package com.dongsan.core.support.error;

import static com.dongsan.core.support.error.CoreErrorStatus.BAD_REQUEST;
import static com.dongsan.core.support.error.CoreErrorStatus.CONFLICT;
import static com.dongsan.core.support.error.CoreErrorStatus.FORBIDDEN;
import static com.dongsan.core.support.error.CoreErrorStatus.NOT_FOUND;

public enum CoreErrorCode {
    // bookmark
    BOOKMARK_NOT_EXIST(NOT_FOUND, "BOOKMARK-01", "해당 북마크가 존재하지 않습니다."),
    SAME_BOOKMARK_NAME_EXIST(CONFLICT, "BOOKMARK-02", "이름이 같은 북마크가 이미 존재합니다."),
    NOT_BOOKMARK_OWNER(FORBIDDEN, "BOOKMARK-03", "해당 북마크의 생성자가 아닙니다."),
    WALKWAY_ALREADY_EXIST_IN_BOOKMARK(CONFLICT, "BOOKMARK-05", "북마크에 이미 존재하는 산책로입니다."),
    WALKWAY_NOT_EXIST_IN_BOOKMARK(NOT_FOUND, "BOOKMARK-06", "북마크에 존재하지 않는 산책로입니다."),

    // image
    IMAGE_NOT_EXISTS(NOT_FOUND, "IMAGE-01", "존재하지 않은 이미지입니다."),

    // walkway
    INVALID_COURSE(BAD_REQUEST, "WALKWAY-01", "유효하지 않은 산책 경로입니다."),
    INVALID_SEARCH_TYPE(BAD_REQUEST, "WALKWAY-02", "유효하지 산책로 검색 타입 입니다."),
    WALKWAY_NOT_FOUND(NOT_FOUND, "WALKWAY-03", "존재하지 않는 산책로 입니다."),
    NOT_WALKWAY_OWNER(FORBIDDEN, "WALKWAY-04", "권한이 없는 산책로 입니다."),
    INVALID_COURSE_IMAGE(BAD_REQUEST, "WALKWAY-05", "유효하지 않은 산책 경로 이미지입니다."),
    WALKWAY_PRIVATE(FORBIDDEN, "WALKWAY-06", "비공개 산책로 입니다."),
    LIKED_WALKWAY_NOT_FOUND(NOT_FOUND, "WALKWAY-07", "좋아하는 산책로로 저장하지 않은 산책로 입니다."),
    CANT_CREATE_WALKWAY(BAD_REQUEST, "WALKWAY-08", "산책로를 생성할 수 없습니다."),
    CANT_CREATE_LIKED_WALKWAY(BAD_REQUEST, "WALKWAY-09", "산책로 좋아요를 생성할 수 없습니다."),
    CANT_DELETE_LIKED_WALKWAY(BAD_REQUEST, "WALKWAY-10", "산책로 좋아요를 삭제할 수 없습니다."),
    HISTORY_NOT_FOUND(NOT_FOUND, "WALKWAY-11", "산책로를 이용한 기록이 없습니다."),
    NOT_ENOUGH_DISTANCE(FORBIDDEN, "WALKWAY-12", "충분히 산책하지 않았습니다."),
    ALREADY_REVIEWED(CONFLICT, "WALKWAY-13", "이미 리뷰를 작성하였습니다."),
    INVALID_ACCESS(FORBIDDEN, "WALKWAY-14", "기록의 산책로와 유저가 다릅니다."),

    // member
    MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER-01", "해당 회원이 존재하지 않습니다."),

    // review
    REVIEW_NOT_FOUND(NOT_FOUND, "REVIEW-01", "해당 리뷰는 존재하지 않습니다."),
    INVALID_SORT_TYPE(BAD_REQUEST, "REVIEW-02", "유효하지 않은 정렬 타입 입니다."),
    NOT_REVIEW_OWNER(FORBIDDEN, "REVIEW-03", "리뷰의 작성자가 아닙니다."),
    CANT_CREATE_REVIEW(BAD_REQUEST, "REVIEW-04", "리뷰를 생성할 수 없습니다."),

    // walkway history
    WALKWAY_HISTORY_NOT_FOUND(NOT_FOUND, "WALKWAY-HISTORY-01", "존재하지 않는 산책로 이용 기록입니다."),
    CANT_CREATE_WALKWAY_HISTORY(BAD_REQUEST, "WALKWAY-HISTORY-02", "산책 기록을 생성할 수 없습니다.")
    ;

    private final CoreErrorStatus httpStatus;
    private final String code;
    private final String message;

    CoreErrorCode(CoreErrorStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public CoreErrorStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }


}
