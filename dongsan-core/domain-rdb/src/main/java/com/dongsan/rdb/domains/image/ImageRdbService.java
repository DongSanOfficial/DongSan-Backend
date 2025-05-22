package com.dongsan.rdb.domains.image;

import com.dongsan.rdb.support.error.CoreErrorCode;
import com.dongsan.rdb.support.error.CoreException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ImageRdbService {
    private final ImageRepository imageRepository;

    public ImageRdbService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Long save(String url) {
        return imageRepository.save(url);
    }

    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.IMAGE_NOT_EXISTS));
    }

}
