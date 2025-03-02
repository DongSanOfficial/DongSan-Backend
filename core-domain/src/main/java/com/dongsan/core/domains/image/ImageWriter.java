package com.dongsan.core.domains.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ImageWriter {
    @Autowired
    public ImageWriter(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private final ImageRepository imageRepository;

    public Long createImage(String imageUrl) {
        return imageRepository.save(imageUrl);
    }
}
