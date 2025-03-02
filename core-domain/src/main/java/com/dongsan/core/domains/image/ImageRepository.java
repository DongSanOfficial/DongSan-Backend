package com.dongsan.core.domains.image;

import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository {
    Image findById(Long imageId);
    Long save(String url);
    boolean existsById(Long imageId);
}
