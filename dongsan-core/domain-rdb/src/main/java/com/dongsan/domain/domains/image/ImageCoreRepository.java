package com.dongsan.domain.domains.image;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    @Override
    public Optional<Image> findByUrl(String url) {
        return imageJpaRepository.findByUrl(url);
    }

}
