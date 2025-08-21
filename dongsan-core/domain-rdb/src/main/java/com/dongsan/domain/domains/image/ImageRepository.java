package com.dongsan.domain.domains.image;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository {
    Optional<Image> findById(Long imageId);

    Long save(String url);

    Optional<Image> findByUrl(String url);
}
