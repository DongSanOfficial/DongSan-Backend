package com.dongsan.rdb.domains.review.domain;

import com.dongsan.rdb.domains.common.BaseEntity;
import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import jakarta.persistence.*;

@Entity
@Table(name = "review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String content;

    private Long walkwayLogId;

    protected Review() {
    }

    public Review(Long walkwayLogId, int rating, String content) {
        validateRating(rating);
        validateContent(content);
        this.rating = rating;
        this.content = content;
        this.walkwayLogId = walkwayLogId;
    }

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new CoreException(CoreErrorCode.INVALID_RATING_VALUE);
        }
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new CoreException(CoreErrorCode.REVIEW_CONTENT_BLANK);
        }
        if (content.length() > 200) {
            throw new CoreException(CoreErrorCode.REVIEW_CONTENT_GT_200);
        }
    }

    public Long getId() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }
}
