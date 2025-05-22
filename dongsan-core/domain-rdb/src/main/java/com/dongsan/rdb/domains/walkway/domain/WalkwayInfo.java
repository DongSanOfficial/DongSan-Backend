package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.walkway.ListStringConverter;
import jakarta.persistence.*;

import java.util.List;

@Embeddable
public class WalkwayInfo {
    @Column(nullable = false)
    private String name;

    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ExposeLevel exposeLevel;

    @Convert(converter = ListStringConverter.class)
    private List<String> hashtags;

    @Column(nullable = false)
    private Double distance;  // km

    @Column(nullable = false)
    private Integer time; // 초

    protected WalkwayInfo() {
    }

    // TODO : 여기 수정
    public WalkwayInfo(String name, Double distance, Integer time, ExposeLevel exposeLevel, String memo, List<String> hashtags) {
        if (name.trim().isEmpty()) {
            throw new CoreException(CoreErrorCode.INVALID_INPUT_VALUE, "산책로 이름은 비어있을 수 없습니다.");
        }
        Objects.requireNonNull(distance, "거리는 필수입니다.");
        Objects.requireNonNull(time, "시간은 필수입니다.");
        Objects.requireNonNull(exposeLevel, "공개 범위는 필수입니다.");

        // hashtags는 null이면 빈 리스트로 초기화 (또는 비즈니스 규칙에 따라 예외 처리)
        if (hashtags == null) {
            this.hashtags = List.of();
        } else {
            this.hashtags = hashtags;
        }

        // 2. 비즈니스 로직에 따른 값의 범위/조건 검증
        if (distance < 0.2) {
            throw new CoreException(CoreErrorCode.INVALID_INPUT_VALUE, "거리는 0.2km 이상이어야 합니다.");
        }
        if (time < 600) { // 10분 = 600초
            throw new CoreException(CoreErrorCode.INVALID_INPUT_VALUE, "시간은 10분(600초) 이상이어야 합니다.");
        }

        // 모든 검증을 통과한 후 필드 할당
        this.name = name;
        this.distance = distance;
        this.time = time;
        this.exposeLevel = exposeLevel;
        this.memo = memo;
        // this.hashtags는 위에서 이미 처리됨
    }

    // 기존 updateInfo 메서드 (업데이트 시에도 유효성 검증 가능)

    /**
     * @NotNull Long courseImageId,
     * @NotBlank(message = "산책로 제목을 입력해주세요.")
     * String name,
     * String memo,
     * @DecimalMin("0.2") Double distance,
     * @Min(600) Integer time,
     * @NotNull List<String> hashtags,
     */
    public void updateInfo(String name, String memo, ExposeLevel exposeLevel, List<String> hashtags) {
        // 업데이트 시 name이 null이거나 비어있지 않아야 한다면:
        if (name != null && name.trim().isEmpty()) {
            throw new CoreException(CoreErrorCode.INVALID_INPUT_VALUE, "산책로 이름은 비어있을 수 없습니다.");
        }
        // exposeLevel이 null이 될 수 없다면
        Objects.requireNonNull(exposeLevel, "공개 범위는 필수입니다.");

        // hashtags는 null이면 빈 리스트로 초기화 (또는 비즈니스 규칙에 따라 예외 처리)
        if (hashtags == null) {
            this.hashtags = List.of();
        } else {
            this.hashtags = hashtags;
        }

        this.name = name;
        this.memo = memo;
        this.exposeLevel = exposeLevel;
        // this.hashtags는 위에서 이미 처리됨
    }

    public void validateExposeLevel() {
        if (this.exposeLevel.equals(ExposeLevel.PRIVATE)) {
            throw new CoreException(CoreErrorCode.WALKWAY_PRIVATE);
        }
    }
}
