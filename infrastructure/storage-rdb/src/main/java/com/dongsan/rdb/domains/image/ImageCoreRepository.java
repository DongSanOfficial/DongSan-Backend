package com.dongsan.rdb.domains.image;


import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.image.ImageRepository;
import com.dongsan.core.support.error.CoreErrorCode;
import com.dongsan.core.support.error.CoreException;
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
    public Image findById(Long imageId) {
        ImageEntity imageEntity = imageJpaRepository.findById(imageId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.IMAGE_NOT_EXISTS));

        return imageEntity.toImage();
    }

    @Override
    public Long save(String url) {
        ImageEntity imageEntity = imageJpaRepository.save(new ImageEntity(url));
        return imageEntity.getId();
    }

    @Override
    public boolean existsById(Long ImageId) {
        return imageJpaRepository.existsById(ImageId);
    }
}
