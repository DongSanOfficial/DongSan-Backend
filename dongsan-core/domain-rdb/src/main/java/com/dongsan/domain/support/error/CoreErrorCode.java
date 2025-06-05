package com.dongsan.domain.support.error;

public enum CoreErrorCode {
    // bookmark
    BOOKMARK_NOT_EXIST(CoreErrorStatus.NOT_FOUND, "BOOKMARK-01", "해당 북마크가 존재하지 않습니다."),
    SAME_BOOKMARK_NAME_EXIST(CoreErrorStatus.CONFLICT, "BOOKMARK-02", "이름이 같은 북마크가 이미 존재합니다."),
    NOT_BOOKMARK_OWNER(CoreErrorStatus.FORBIDDEN, "BOOKMARK-03", "해당 북마크의 생성자가 아닙니다."),
    WALKWAY_ALREADY_EXIST_IN_BOOKMARK(CoreErrorStatus.CONFLICT, "BOOKMARK-04", "북마크에 이미 존재하는 산책로입니다."),
    WALKWAY_NOT_EXIST_IN_BOOKMARK(CoreErrorStatus.NOT_FOUND, "BOOKMARK-05", "북마크에 존재하지 않는 산책로입니다."),

    // image
    IMAGE_NOT_EXISTS(CoreErrorStatus.NOT_FOUND, "IMAGE-01", "존재하지 않은 이미지입니다."),

    // walkway
    INVALID_SEARCH_TYPE(CoreErrorStatus.BAD_REQUEST, "WALKWAY-01", "유효하지 산책로 검색 타입 입니다."),
    WALKWAY_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "WALKWAY-02", "존재하지 않는 산책로 입니다."),
    NOT_WALKWAY_OWNER(CoreErrorStatus.FORBIDDEN, "WALKWAY-03", "권한이 없는 산책로 입니다."),
    WALKWAY_CANT_ACCESS(CoreErrorStatus.FORBIDDEN, "WALKWAY-04", "접근할 수 없는 산책로 입니다. (타인이 등록한 비공개 산책로)"),
    ALREADY_LIKED_WALKWAY(CoreErrorStatus.BAD_REQUEST, "WALKWAY-05", "이미 좋아요를 한 산책로입니다."),
    NOT_LIKED_WALKWAY(CoreErrorStatus.BAD_REQUEST, "WALKWAY-06", "좋아요를 누른적 없는 산책로입니다."),
    NOT_ENOUGH_DISTANCE(CoreErrorStatus.FORBIDDEN, "WALKWAY-07", "충분히 산책하지 않았습니다."),
    ALREADY_REVIEWED(CoreErrorStatus.CONFLICT, "WALKWAY-08", "이미 리뷰를 작성하였습니다."),
    WALKWAY_NAME_NOT_BLANK(CoreErrorStatus.BAD_REQUEST, "WALKWAY-10", "산책로 이름은 공백일 수 없습니다."),
    WALKWAY_DISTANCE_NOT_ENOUGH(CoreErrorStatus.BAD_REQUEST, "WALKWAY-11", "산책로 등록 가능 거리는 0.2km 이상입니다."),
    WALKWAY_TIME_NOT_ENOUGH(CoreErrorStatus.BAD_REQUEST, "WALKWAY-12", "산책로 등록 가능 시간은 10분(600초) 이상 입니다."),

    // member
    MEMBER_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "MEMBER-01", "해당 회원이 존재하지 않습니다."),

    // review
    REVIEW_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "REVIEW-01", "해당 리뷰는 존재하지 않습니다."),
    INVALID_SORT_TYPE(CoreErrorStatus.BAD_REQUEST, "REVIEW-02", "유효하지 않은 정렬 타입 입니다."),
    INVALID_RATING_VALUE(CoreErrorStatus.BAD_REQUEST, "REVIEW-03", "별점은 1 이상 5 이하의 정수입니다."),
    REVIEW_CONTENT_BLANK(CoreErrorStatus.BAD_REQUEST, "REVIEW-04", "리뷰 내용을 필수로 작성해야합니다."),
    REVIEW_CONTENT_GT_200(CoreErrorStatus.BAD_REQUEST, "REVIEW-05", "리뷰 내용은 200자를 넘길 수 없습니다."),

    // walkway log
    WALKWAY_LOG_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "WALKWAY-LOG-01", "존재하지 않는 산책로 이용 기록입니다."),
    INVALID_OWNER_AND_WALKWAY(CoreErrorStatus.FORBIDDEN, "WALKWAY-LOG-02", "기록의 산책로와 유저가 다릅니다."),
    WALKWAY_HISTORY_TIME_NOT_ENOUGH(CoreErrorStatus.BAD_REQUEST, "WALKWAY-LOG-03", "산책 기록 등록 가능 거리는 0초 이상 입니다."),
    WALKWAY_HISTORY_DISTANCE_NOT_ENOUGH(CoreErrorStatus.BAD_REQUEST, "WALKWAY-LOG-04", "산책 기록 등록 가능 시간은 0km 이상 입니다."),

    // auth provider
    PROVIDER_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "PROVIDER-01", "존재하지 않는 Provider 입니다."),

    // crew
    CREW_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "CREW-01", "존재하지 않는 크루 입니다."),
    CREW_MEMBER_LIMIT_NOT_VALID(CoreErrorStatus.BAD_REQUEST, "CREW-02", "크루 제한 가능 인원은 2명 이상 100명 이하입니다."),
    CREW_NAME_NOT_VALID(CoreErrorStatus.BAD_REQUEST, "CREW-03", "크루 이름은 1자 이상 20자 이하 입니다."),
    CREW_DESCRIPTION_NOT_VALID(CoreErrorStatus.BAD_REQUEST, "CREW-04", "크루 설명은 250자 이하 입니다."),
    CREW_RULE_NOT_VALID(CoreErrorStatus.BAD_REQUEST, "CREW-05", "크루 규칙은 250자 이하 입니다."),
    PRIVATE_CREW_PASSWORD_NOT_VALID(CoreErrorStatus.BAD_REQUEST, "CREW-06",
            "비공개 크루는 비밀번호를 필수로 입력해야하고, 8자 이상 20자 이하입니다."),
    CREW_NAME_DUPLICATED(CoreErrorStatus.BAD_REQUEST, "CREW-07", "동일한 크루 이름이 이미 존재합니다."),
    CREW_NOT_JOINED(CoreErrorStatus.FORBIDDEN, "CREW-08", "해당 크루에 가입되어 있지 않습니다."),
    CREW_ALREADY_JOINED(CoreErrorStatus.CONFLICT, "CREW-09", "이미 해당 크루에 가입되어 있습니다."),
    CREW_CANT_ACCESS(CoreErrorStatus.FORBIDDEN, "CREW-10", "해당 크루에 접근할 수 없습니다. (가입하지 않은 비공개크루)"),
    CREW_INVALID_SORT(CoreErrorStatus.BAD_REQUEST, "CREW-11", "존재하지 않는 정렬 방식 입니다. (지원 정렬 방식 : distance, duration"),
    CREW_INVALID_PERIOD(CoreErrorStatus.BAD_REQUEST, "CREW-12", "지원하지 않는 기간 입니다. (지원 기간 : daily, weekly, monthly"),
    CREW_MEMBER_FULL(CoreErrorStatus.CONFLICT, "CREW-13", "가입 가능 인원이 다 차서 가입할 수 없습니다."),
    CREW_PASSWORD_INVALID(CoreErrorStatus.CONFLICT, "CREW-14", "비밀번호가 일치하지 않습니다."),
    CREW_NOT_MANAGER(CoreErrorStatus.FORBIDDEN, "CREW-14", "크루의 메니저가 아닙니다."),
    CREW_LIMIT_LT_MEMBER(CoreErrorStatus.FORBIDDEN, "CREW-15", "가입 제한 인원을 크루에 이미 가입한 사용자 수 보다 적은 인원으로 설정할 수 없습니다."),


    // cowalk
    COWALK_NOT_FOUND(CoreErrorStatus.NOT_FOUND, "COWALK-01", "존재하지 않는 같이 산책 입니다."),
    COWALK_PARTICIPANT_ALREADY_JOIN(CoreErrorStatus.BAD_REQUEST, "COWALK-02", "이미 같이 산책에 참여했습니다."),
    COWALK_PARTICIPANT_LIMIT(CoreErrorStatus.BAD_REQUEST, "COWALK-03", "같이 산책 인원이 이미 모두 찼습니다."),
    COWALK_LOCK_FAIL(CoreErrorStatus.CONFLICT, "COWALK-04", "같이 산책 참여에 실패했습니다."),
    COWALK_PARTICIPANT_NOT_JOIN(CoreErrorStatus.BAD_REQUEST, "COWALK-05", "같이 산책에 참여하지 않은 사용자입니다."),

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

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
