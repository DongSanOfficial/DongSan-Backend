package com.dongsan.rdb.domains.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ImageCoreRepository implements ImageRepository {
    private final ImageJpaRepository imageJpaRepository;

    @Autowired
    public ImageCoreRepository(ImageJpaRepository imageJpaRepository) {
        this.imageJpaRepository = imageJpaRepository;
    }

    @Override
    public Optional<Image> findById(Long imageId) {
        return imageJpaRepository.findById(imageId);
    }

    @Override
    public Long save(String url) {
        Image image = imageJpaRepository.save(new Image(url));
        return image.getId();
    }
    
}
