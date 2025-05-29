package com.dongsan.api.domains.crew;

import com.dongsan.api.domains.crew.dto.request.CreateCrewRequest;
import com.dongsan.api.domains.crew.dto.response.CreateCrewImageResponse;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.crew.service.CrewRdbService;
import com.dongsan.domain.domains.image.ImageRdbService;
import com.dongsan.file.service.S3FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class CrewInfoFacade {
    private final CrewRdbService crewRdbService;
    private final CrewMemberRdbService crewMemberRdbService;
    private final S3FileService s3FileService;
    private final ImageRdbService imageRdbService;

    public CrewInfoFacade(CrewRdbService crewRdbService, CrewMemberRdbService crewMemberRdbService, S3FileService s3FileService, ImageRdbService imageRdbService) {
        this.crewRdbService = crewRdbService;
        this.crewMemberRdbService = crewMemberRdbService;
        this.s3FileService = s3FileService;
        this.imageRdbService = imageRdbService;
    }

    @Transactional(readOnly = true)
    public boolean isNameDuplicated(String name) {
        name = name.trim();
        return crewRdbService.isNameDuplicated(name);
    }

    @Transactional
    public Long saveCrew(CreateCrewRequest request, Long memberId) {
        String imageUrl = request.crewImageId() == null ? null : imageRdbService.getImage(request.crewImageId()).getUrl();
        Long crewId = crewRdbService.save(request.toCreateCrewCommand(imageUrl));
        crewMemberRdbService.saveManager(crewId, memberId);
        return crewId;
    }

    @Transactional
    public CreateCrewImageResponse saveImage(MultipartFile image) {
        String imageUrl = s3FileService.saveFile(image);
        Long imageId = imageRdbService.save(imageUrl);
        return new CreateCrewImageResponse(imageId, imageUrl);
    }

    public void leaveCrew(Long crewId, Long memberId) {
        crewRdbService.getCrew(crewId);
        crewMemberRdbService.leaveCrew(crewId, memberId);
    }
}
