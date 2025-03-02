package com.dongsan.core.domains.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class ImageReader {
    @Autowired
    public ImageReader(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private final ImageRepository imageRepository;

    public Image getImage(Long id) {
        return imageRepository.findById(id);
    }
}
