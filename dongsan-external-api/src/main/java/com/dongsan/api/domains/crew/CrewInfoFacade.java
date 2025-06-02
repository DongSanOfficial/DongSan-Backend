package com.dongsan.api.domains.crew;

import com.dongsan.api.domains.crew.dto.request.CreateCrewRequest;
import com.dongsan.api.domains.crew.dto.response.CreateCrewImageResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewFeedResponse;
import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.crew.service.CrewRdbService;
import com.dongsan.domain.domains.image.ImageRdbService;
import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.domains.member.MemberRdbService;
import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.domain.support.util.CursorPage;
import com.dongsan.domain.support.util.CursorRequest;
import com.dongsan.file.service.S3FileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CrewInfoFacade {
    private final CrewRdbService crewRdbService;
    private final CrewMemberRdbService crewMemberRdbService;
    private final WalkwayLogRdbService walkwayLogRdbService;
    private final MemberRdbService memberRdbService;
    private final S3FileService s3FileService;
    private final ImageRdbService imageRdbService;

    public CrewInfoFacade(CrewRdbService crewRdbService, CrewMemberRdbService crewMemberRdbService, WalkwayLogRdbService walkwayLogRdbService, MemberRdbService memberRdbService, S3FileService s3FileService, ImageRdbService imageRdbService) {
        this.crewRdbService = crewRdbService;
        this.crewMemberRdbService = crewMemberRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
        this.memberRdbService = memberRdbService;
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

    @Transactional
    public void leaveCrew(Long crewId, Long memberId) {
        crewRdbService.getCrew(crewId);
        crewMemberRdbService.leaveCrew(crewId, memberId);
    }

    @Transactional(readOnly = true)
    public CursorPage<GetCrewFeedResponse> getCrewFeed(Long crewId, Long memberId, CursorRequest paging) {
        Crew crew = crewRdbService.getCrew(crewId);

        boolean isCrewMember = crewMemberRdbService.isCrewMember(crewId, memberId);
        crew.canAccess(isCrewMember);

        CursorPage<WalkwayLog> walkwayLogs = walkwayLogRdbService.getCrewFeed(crewId, paging.lastId(), paging.size());
        List<Long> memberIds = walkwayLogs.getData().stream().map(WalkwayLog::getMemberId).toList();
        Map<Long, Member> memberMap = memberRdbService.getMemberMap(memberIds);
        List<GetCrewFeedResponse> result = GetCrewFeedResponse.from(walkwayLogs, memberMap);
        return new CursorPage<>(result, walkwayLogs.getHasNext());
    }
}
