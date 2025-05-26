package com.dongsan.rdb.domains.walkway.domain;

import com.dongsan.rdb.domains.walkway.ListStringConverter;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Embeddable
public class WalkwayInfo {
    private static final double MIN_DISTANCE_KM = 0.2;
    private static final int MIN_TIME_SEC = 600;

    @Column(nullable = false)
    private String name;

    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ExposeLevel exposeLevel;

    @Convert(converter = ListStringConverter.class)
    private List<String> hashtags;

    @Column(nullable = false, name = "distance")
    private Double distanceKm;

    @Column(nullable = false, name = "time")
    private Integer timeSec;

    protected WalkwayInfo() {
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new CoreException(CoreErrorCode.WALKWAY_NAME_NOT_BLANK);
        }
    }

    private void validateDistance(Double distance) {
        if (distance == null || distance < MIN_DISTANCE_KM) {
            throw new CoreException(CoreErrorCode.WALKWAY_DISTANCE_NOT_ENOUGH);
        }
    }

    private void validateTime(Integer time) {
        if (time == null || time < MIN_TIME_SEC) {
            throw new CoreException(CoreErrorCode.WALKWAY_TIME_NOT_ENOUGH);
        }
    }

    public WalkwayInfo(String name, Double distanceKm, Integer timeSec, ExposeLevel exposeLevel, String memo, List<String> hashtags) {
        name = name.trim();
        memo = memo.trim();

        validateName(name);
        validateDistance(distanceKm);
        validateTime(timeSec);

        this.name = name;
        this.distanceKm = distanceKm;
        this.timeSec = timeSec;
        this.exposeLevel = exposeLevel;
        this.memo = memo;
        this.hashtags = Objects.requireNonNullElseGet(hashtags, List::of);
    }

    public void updateWalkwayInfo(String name, String memo, ExposeLevel exposeLevel, List<String> hashtags) {
        updateName(name);
        updateMemo(memo);
        updateExposeLevel(exposeLevel);
        updateHashtag(hashtags);
    }

    private void updateName(String name) {
        name = name.trim();
        validateName(name);
        this.name = name;
    }

    private void updateMemo(String memo) {
        memo = memo.trim();
        this.memo = memo;
    }

    private void updateExposeLevel(ExposeLevel exposeLevel) {
        this.exposeLevel = exposeLevel;
    }

    private void updateHashtag(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public String getName() {
        return name;
    }

    public String getMemo() {
        return memo;
    }

    public ExposeLevel getExposeLevel() {
        return exposeLevel;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public Integer getTimeSec() {
        return timeSec;
    }
}
