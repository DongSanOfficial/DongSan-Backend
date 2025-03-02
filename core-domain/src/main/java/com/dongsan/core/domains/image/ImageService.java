package com.dongsan.core.domains.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {
    private final ImageWriter imageWriter;
    private final ImageReader imageReader;
    private final ImageValidator imageValidator;
    @Autowired
    public ImageService(ImageWriter imageWriter, ImageReader imageReader, ImageValidator imageValidator) {
        this.imageWriter = imageWriter;
        this.imageReader = imageReader;
        this.imageValidator = imageValidator;
    }

    @Transactional
    public Long createImage(String url) {
        return imageWriter.createImage(url);
    }

    public Image getImage(Long imageId) {
        imageValidator.validateImageExists(imageId);
        return imageReader.getImage(imageId);
    }
}
