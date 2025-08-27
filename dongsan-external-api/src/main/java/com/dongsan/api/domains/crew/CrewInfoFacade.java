package com.dongsan.api.domains.crew;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dongsan.api.domains.crew.dto.request.CreateUpdateCrewRequest;
import com.dongsan.api.domains.crew.dto.response.CreateCrewImageResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewFeedResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewInfoResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewMemberRankingResponse;
import com.dongsan.api.domains.crew.dto.response.GetCrewsResponse;
import com.dongsan.api.domains.crew.dto.response.GetMyCrewIdsResponse;
import com.dongsan.api.support.util.DateRangeUtil;
import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewMember;
import com.dongsan.domain.domains.crew.domain.CrewMemberStatistic;
import com.dongsan.domain.domains.crew.domain.CrewWeeklyStatistic;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.crew.service.CrewRdbService;
import com.dongsan.domain.domains.image.ImageRdbService;
import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.domains.member.MemberRdbService;
import com.dongsan.domain.domains.walkwayLog.WalkwayLog;
import com.dongsan.domain.domains.walkwayLog.WalkwayLogRdbService;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;
import com.dongsan.file.service.S3FileService;

@Service
public class CrewInfoFacade {
    private final CrewRdbService crewRdbService;
    private final CrewMemberRdbService crewMemberRdbService;
    private final WalkwayLogRdbService walkwayLogRdbService;
    private final MemberRdbService memberRdbService;
    private final S3FileService s3FileService;
    private final ImageRdbService imageRdbService;
    private final CrewJoinFacade crewJoinFacade;

    public CrewInfoFacade(CrewRdbService crewRdbService, CrewMemberRdbService crewMemberRdbService,
                          WalkwayLogRdbService walkwayLogRdbService, MemberRdbService memberRdbService, S3FileService s3FileService,
                          ImageRdbService imageRdbService, CrewJoinFacade crewJoinFacade) {
        this.crewRdbService = crewRdbService;
        this.crewMemberRdbService = crewMemberRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
        this.memberRdbService = memberRdbService;
        this.s3FileService = s3FileService;
        this.imageRdbService = imageRdbService;
        this.crewJoinFacade = crewJoinFacade;
    }

    @Transactional(readOnly = true)
    public boolean isNameUnique(String name, Long crewId) {
        name = name.trim();
        if (crewId == null) {
            return !crewRdbService.isNameDuplicated(name);
        }
        return !crewRdbService.isNameDuplicatedExceptSelf(name, crewId);
    }

    @Transactional
    public Long saveCrew(CreateUpdateCrewRequest request, Long memberId) {
        String imageUrl =
                request.crewImageId() == null ? null : imageRdbService.getImage(request.crewImageId()).getUrl();
        Long crewId = crewRdbService.save(request.toCrewInfoCommand(imageUrl));
        crewMemberRdbService.saveCrewManager(crewId, memberId);
        return crewId;
    }

    @Transactional
    public void updateCrew(CreateUpdateCrewRequest request, Long crewId, Long memberId) {
        Crew crew = crewRdbService.getCrew(crewId);
        crewMemberRdbService.validateIsCrewManager(crewId, memberId);
        int memberCount = crewMemberRdbService.countCrewMember(crewId);
        if (request.memberLimit() != null && memberCount > request.memberLimit()) {
            throw new CoreException(CoreErrorCode.CREW_LIMIT_LT_MEMBER);
        }
        String imageUrl =
                request.crewImageId() == null ? null : imageRdbService.getImage(request.crewImageId()).getUrl();
        crewRdbService.update(crew, request.toCrewInfoCommand(imageUrl));
    }

    @Transactional
    public CreateCrewImageResponse saveImage(MultipartFile image) {
        String imageUrl = s3FileService.saveFile(image);
        Long imageId = imageRdbService.save(imageUrl);
        return new CreateCrewImageResponse(imageId, imageUrl);
    }

    @Transactional(readOnly = true)
    public CursorResponse<GetCrewFeedResponse> getCrewFeed(Long crewId, Long memberId, CursorRequest paging) {
        Crew crew = crewRdbService.getCrew(crewId);
        boolean isCrewMember = crewMemberRdbService.isCrewMember(crewId, memberId);
        crew.canAccess(isCrewMember);

        CursorResponse<WalkwayLog> walkwayLogs = walkwayLogRdbService.getCrewFeed(crewId, crew.getCreatedAt(), paging.lastId(),
                paging.size());
        List<Long> memberIds = walkwayLogs.data().stream().map(WalkwayLog::getMemberId).toList();
        Map<Long, Member> memberMap = memberRdbService.getMemberMap(memberIds);
        List<GetCrewFeedResponse> result = GetCrewFeedResponse.from(walkwayLogs, memberMap);
        return new CursorResponse<>(result, walkwayLogs.hasNext());
    }

    @Transactional(readOnly = true)
    public GetCrewInfoResponse getCrewInfo(Long crewId, Long memberId) {
        Crew crew = crewRdbService.getCrew(crewId);
        boolean isCrewMember = crewMemberRdbService.isCrewMember(crewId, memberId);
        crew.canAccess(isCrewMember);

        int memberCount = crewMemberRdbService.countCrewMember(crewId);
        LocalDate startDate = DateRangeUtil.getStartOfWeek(LocalDate.now());
        LocalDate endDate = DateRangeUtil.getEndOfWeek(LocalDate.now());
        CrewWeeklyStatistic crewWeeklyStat = walkwayLogRdbService.getCrewWeeklyStat(crewId, startDate, endDate);
        Long imageId = crew.getCrewImageUrl() == null
                ? null
                : imageRdbService.getImageByUrl(crew.getCrewImageUrl()).getId();
        Long managerId = crewMemberRdbService.getManager(crew.getId()).getMemberId();
        Member manager = memberRdbService.getMember(managerId);
        return new GetCrewInfoResponse(crew, memberCount, crewWeeklyStat, isCrewMember, imageId, manager);
    }

    @Transactional(readOnly = true)
    public CursorResponse<GetCrewMemberRankingResponse> getCrewMemberRanking(Long crewId, Long memberId, LocalDate date,
                                                                             CrewRankingSort sort, CrewRankingPeriod period, CursorRequest paging) {
        Crew crew = crewRdbService.getCrew(crewId);
        boolean isCrewMember = crewMemberRdbService.isCrewMember(crewId, memberId);
        crew.canAccess(isCrewMember);

        LocalDate startDay = DateRangeUtil.getStartDay(date, period);
        LocalDate endDay = DateRangeUtil.getEndDay(date, period);
        CursorResponse<CrewMemberStatistic> response = switch (sort) {
            case DISTANCE -> walkwayLogRdbService.getCrewRankingByDistance(crewId, paging.lastId(), startDay, endDay,
                    paging.size());
            case DURATION ->
                    walkwayLogRdbService.getCrewRankingByTime(crewId, paging.lastId(), startDay, endDay, paging.size());
        };
        List<Long> memberIds = response.data().stream().map(CrewMemberStatistic::memberId).toList();
        Map<Long, Member> memberMap = memberRdbService.getMemberMap(memberIds);
        List<GetCrewMemberRankingResponse> result = GetCrewMemberRankingResponse.from(response.data(), memberMap);
        return new CursorResponse<>(result, response.hasNext());
    }

    @Transactional
    public void leaveCrew(Long crewId, Long memberId) {
        crewRdbService.getCrew(crewId);
        crewMemberRdbService.leaveCrew(crewId, memberId);
    }

    public void joinCrew(Long crewId, Long memberId, String password) {
        Crew crew = crewRdbService.getCrew(crewId);
        if (crew.isLimitedCrew()) {
            crewJoinFacade.joinLimitedCrew(crew, memberId, password);
        } else {
            crewJoinFacade.joinUnLimitedCrew(crew, memberId, password);
        }
    }

    public CursorResponse<GetCrewsResponse> getMyCrews(Long memberId, CursorRequest paging) {
        CursorResponse<Crew> crews = crewRdbService.getMyCrews(memberId, paging.size(), paging.lastId());
        List<GetCrewsResponse> result = this.getCrewsResponseList(crews.data(), memberId);
        return new CursorResponse<>(result, crews.hasNext());
    }

    public CursorResponse<GetCrewsResponse> searchCrews(Long memberId, String name, CursorRequest paging) {
        CursorResponse<Crew> crews = crewRdbService.searchCrews(name, paging.size(), paging.lastId());
        List<GetCrewsResponse> result = this.getCrewsResponseList(crews.data(), memberId);
        return new CursorResponse<>(result, crews.hasNext());
    }

    public CursorResponse<GetCrewsResponse> recommendCrews(Long memberId, CursorRequest paging) {
        CursorResponse<Crew> crews = crewRdbService.recommendCrews(paging.size(), paging.lastId(), memberId);
        List<GetCrewsResponse> result = this.getCrewsResponseList(crews.data(), memberId);
        return new CursorResponse<>(result, crews.hasNext());
    }

    private List<GetCrewsResponse> getCrewsResponseList(List<Crew> crewList, Long memberId) {
        List<Long> crewIds = crewList.stream().map(Crew::getId).toList();

        Map<Long, CrewMember> crewMemberMap = crewMemberRdbService.findByCrewIdAndMemberId(crewIds, memberId);
        Map<Long, Integer> memberCountMap = crewMemberRdbService.countByCrewIds(crewIds);

        return GetCrewsResponse.from(crewList, crewMemberMap, memberCountMap);
    }

    public GetMyCrewIdsResponse getMyCrewIds(Long memberId) {
        List<Long> myCrewIds = crewRdbService.getMyCrewIds(memberId);
        return new GetMyCrewIdsResponse(myCrewIds);
    }
}
