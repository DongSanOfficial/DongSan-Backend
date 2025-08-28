package com.dongsan.api.support.factory;

import com.dongsan.domain.domains.review.domain.Review;
import com.dongsan.domain.domains.review.domain.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewFactory {
    @Autowired
    private ReviewRepository reviewRepository;

    public Long save(Long walkwayLog, int rating, String content) {
        return reviewRepository.save(new Review(walkwayLog, rating, content));
    }
}
