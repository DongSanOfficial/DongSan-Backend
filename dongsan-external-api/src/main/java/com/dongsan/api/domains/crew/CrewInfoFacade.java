package com.dongsan.api.domains.crew;

import com.dongsan.api.domains.crew.dto.request.CreateUpdateCrewRequest;
import com.dongsan.api.domains.crew.dto.response.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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

    public CrewInfoFacade(CrewRdbService crewRdbService, CrewMemberRdbService crewMemberRdbService,
                          WalkwayLogRdbService walkwayLogRdbService, MemberRdbService memberRdbService, S3FileService s3FileService,
                          ImageRdbService imageRdbService) {
        this.crewRdbService = crewRdbService;
        this.crewMemberRdbService = crewMemberRdbService;
        this.walkwayLogRdbService = walkwayLogRdbService;
        this.memberRdbService = memberRdbService;
        this.s3FileService = s3FileService;
        this.imageRdbService = imageRdbService;
    }

    @Transactional(readOnly = true)
    public boolean isNameUnique(String name) {
        name = name.trim();
        return !crewRdbService.isNameDuplicated(name);
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

        CursorResponse<WalkwayLog> walkwayLogs = walkwayLogRdbService.getCrewFeed(crewId, paging.lastId(),
                paging.size());
        List<Long> memberIds = walkwayLogs.getData().stream().map(WalkwayLog::getMemberId).toList();
        Map<Long, Member> memberMap = memberRdbService.getMemberMap(memberIds);
        List<GetCrewFeedResponse> result = GetCrewFeedResponse.from(walkwayLogs, memberMap);
        return new CursorResponse<>(result, walkwayLogs.getHasNext());
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
        return new GetCrewInfoResponse(crew, memberCount, crewWeeklyStat, isCrewMember);
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
        List<Long> memberIds = response.getData().stream().map(CrewMemberStatistic::memberId).toList();
        Map<Long, Member> memberMap = memberRdbService.getMemberMap(memberIds);
        List<GetCrewMemberRankingResponse> result = GetCrewMemberRankingResponse.from(response.getData(), memberMap);
        return new CursorResponse<>(result, response.getHasNext());
    }

    @Transactional
    public void leaveCrew(Long crewId, Long memberId) {
        crewRdbService.getCrew(crewId);
        crewMemberRdbService.leaveCrew(crewId, memberId);
    }

    @Transactional
    public void joinCrew(Long crewId, Long memberId, String password) {
        Crew crew = crewRdbService.getCrew(crewId);
        if (crew.isLimitedCrew()) {
            joinLimitedCrew(crewId, memberId, password);
        } else {
            joinUnLimitedCrew(crew, memberId, password);
        }
    }

    private void joinUnLimitedCrew(Crew crew, Long memberId, String password) {
        crewMemberRdbService.validateNotAlreadyJoined(crew.getId(), memberId);
        crewRdbService.comparePassword(crew, password);
        crewMemberRdbService.joinCrew(crew.getId(), memberId);
    }

    private void joinLimitedCrew(Long crewId, Long memberId, String password) {
        Crew crew = crewRdbService.getCrewWithLock(crewId);
        int memberCount = crewMemberRdbService.countCrewMember(crewId);

        crew.validateNotFull(memberCount);
        crewMemberRdbService.validateNotAlreadyJoined(crewId, memberId);
        crewRdbService.comparePassword(crew, password);
        crewMemberRdbService.joinCrew(crewId, memberId);
    }

    public CursorResponse<GetCrewsResponse> getMyCrews(Long memberId, CursorRequest paging) {
        CursorResponse<Crew> crews = crewRdbService.getMyCrews(memberId, paging.size(), paging.lastId());
        List<GetCrewsResponse> result = this.getCrewsResponseList(crews.getData(), memberId);
        return new CursorResponse<>(result, crews.getHasNext());
    }

    public CursorResponse<GetCrewsResponse> searchCrews(Long memberId, String name, CursorRequest paging) {
        CursorResponse<Crew> crews = crewRdbService.searchCrews(name, paging.size(), paging.lastId());
        List<GetCrewsResponse> result = this.getCrewsResponseList(crews.getData(), memberId);
        return new CursorResponse<>(result, crews.getHasNext());
    }

    public CursorResponse<GetCrewsResponse> recommendCrews(Long memberId, CursorRequest paging) {
        CursorResponse<Crew> crews = crewRdbService.recommendCrews(paging.size(), paging.lastId());
        List<GetCrewsResponse> result = this.getCrewsResponseList(crews.getData(), memberId);
        return new CursorResponse<>(result, crews.getHasNext());
    }

    private List<GetCrewsResponse> getCrewsResponseList(List<Crew> crewList, Long memberId) {
        List<Long> crewIds = crewList.stream().map(Crew::getId).toList();

        Map<Long, CrewMember> crewMemberMap = crewMemberRdbService.findByCrewIdAndMemberId(crewIds, memberId);
        Map<Long, Integer> memberCountMap = crewMemberRdbService.countByCrewIds(crewIds);

        return GetCrewsResponse.from(crewList, crewMemberMap, memberCountMap);
    }
}
