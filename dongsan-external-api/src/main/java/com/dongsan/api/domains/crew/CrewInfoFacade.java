package com.dongsan.api.domains.crew;

import com.dongsan.api.domains.crew.dto.response.CreateCrewImageResponse;
import com.dongsan.domain.domains.image.ImageRdbService;
import com.dongsan.file.service.S3FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CrewInfoFacade {
    private final S3FileService s3FileService;
    private final ImageRdbService imageRdbService;

    public CrewInfoFacade(S3FileService s3FileService, ImageRdbService imageRdbService) {
        this.s3FileService = s3FileService;
        this.imageRdbService = imageRdbService;
    }

    @Transactional
    public CreateCrewImageResponse saveImage(MultipartFile image) {
        String imageUrl = s3FileService.saveFile(image);
        Long imageId = imageRdbService.save(imageUrl);
        return new CreateCrewImageResponse(imageId, imageUrl);
    }
}
