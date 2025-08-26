package com.dongsan.api.support.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dongsan.domain.domains.image.ImageRepository;

@Component
public class ImageFactory {
    String DEFAULT_IMAGE_URL = "test image url";

    @Autowired
    private ImageRepository imageRepository;

    public Long save() {
        return imageRepository.save(DEFAULT_IMAGE_URL);
    }
}
