package com.dongsan.domain.domains.image;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository {
    Optional<Image> findById(Long imageId);

    Long save(String url);
}
