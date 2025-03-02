package com.dongsan.core.domains.image;

import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageValidator {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageValidator(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public void validateImageExists(Long imageId) {
        boolean result = imageRepository.existsById(imageId);
        if (!result) {
            throw new CoreException(CoreErrorCode.IMAGE_NOT_EXISTS);
        }
    }
}
