package com.dongsan.core.domains.review;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewWriter {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewWriter(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Long createReview(CreateReview createReview) {
        return reviewRepository.save(createReview);
    }
}
