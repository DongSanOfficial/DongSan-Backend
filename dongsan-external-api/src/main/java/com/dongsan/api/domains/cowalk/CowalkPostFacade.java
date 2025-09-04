package com.dongsan.api.domains.cowalk;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.api.domains.cowalk.dto.request.CreateCowalkCommentRequest;
import com.dongsan.api.domains.cowalk.dto.request.CreateCowalkPostRequest;
import com.dongsan.api.domains.cowalk.dto.response.CowalkCommentResponse;
import com.dongsan.api.domains.cowalk.dto.response.CowalkPostDetailResponse;
import com.dongsan.api.domains.cowalk.dto.response.CowalkPostsResponse;
import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.domain.CowalkComment;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.service.CowalkCommentRdbService;
import com.dongsan.domain.domains.cowalk.service.CowalkParticipantRdbService;
import com.dongsan.domain.domains.cowalk.service.CowalkPostRdbService;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.domains.member.MemberRdbService;
import com.dongsan.domain.lock.CowalkPostLockService;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.paging.CursorResponse;

@Service
public class CowalkPostFacade {
    private final CowalkParticipantRdbService cowalkParticipantRdbService;
    private final CowalkCommentRdbService cowalkCommentRdbService;
    private final CowalkPostRdbService cowalkPostRdbService;
    private final CrewMemberRdbService crewMemberRdbService;
    private final MemberRdbService memberRdbService;
    private final CowalkPostLockService cowalkPostLockService;

    public CowalkPostFacade(
            CowalkParticipantRdbService cowalkParticipantRdbService,
            CowalkCommentRdbService cowalkCommentRdbService,
            CowalkPostRdbService cowalkPostRdbService,
            CrewMemberRdbService crewMemberRdbService,
            MemberRdbService memberRdbService,
            CowalkPostLockService cowalkPostLockService) {
        this.cowalkParticipantRdbService = cowalkParticipantRdbService;
        this.cowalkCommentRdbService = cowalkCommentRdbService;
        this.cowalkPostRdbService = cowalkPostRdbService;
        this.crewMemberRdbService = crewMemberRdbService;
        this.memberRdbService = memberRdbService;
        this.cowalkPostLockService = cowalkPostLockService;
    }

    @Transactional
    public Long saveCowalkPost(CreateCowalkPostRequest createCowalkPostRequest, Long crewId, Long memberId) {
        crewMemberRdbService.validateIsCrewMember(crewId, memberId);
        LocalDate endDate = calculateEndDate(createCowalkPostRequest.startDate(), createCowalkPostRequest.startTime(),
                createCowalkPostRequest.endTime());
        CreateCowalkPostCommand command = createCowalkPostRequest.toCreateCowalkPostCommand(crewId, memberId, endDate);
        Long cowalkPostId = cowalkPostRdbService.save(command);
        cowalkParticipantRdbService.save(memberId, cowalkPostId);
        return cowalkPostId;
    }

    public CowalkPostDetailResponse getCowalkPostDetail(Long cowalkPostId, Long crewId, Long memberId) {
        crewMemberRdbService.validateIsCrewMember(crewId, memberId);
        CowalkPost cowalkPost = cowalkPostRdbService.getCowalkPost(cowalkPostId);
        Integer participantCount = cowalkParticipantRdbService.countByCowalkPostId(cowalkPostId);
        Integer commentCount = cowalkCommentRdbService.countByCowalkPostId(cowalkPostId);
        Member member = memberRdbService.getMember(cowalkPost.getMemberId());
        return new CowalkPostDetailResponse(cowalkPost, participantCount, commentCount, member);
    }

    public Long joinCowalkPost(Long crewId, Long cowalkPostId, Long memberId) {
        crewMemberRdbService.validateIsCrewMember(crewId, memberId);
        try {
            return cowalkPostLockService.executeWithFairLock(cowalkPostId, () -> {
                Integer participantCount = cowalkParticipantRdbService.countByCowalkPostId(cowalkPostId);
                cowalkPostRdbService.validCapacity(cowalkPostId, participantCount);
                return cowalkParticipantRdbService.save(memberId, cowalkPostId);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CoreException(CoreErrorCode.COWALK_LOCK_FAIL);
        }
    }

    public CursorResponse<CowalkPostsResponse> getCowalkPosts(Long crewId, Integer size, Long lastId) {
        CursorResponse<CowalkPost> cowalkPosts = cowalkPostRdbService.getCowalkPosts(size, lastId, crewId);
        List<CowalkPost> cowalkPostList = cowalkPosts.data();

        List<Long> memberIds = cowalkPostList.stream()
                .map(CowalkPost::getMemberId)
                .toList();

        List<Long> cowalkPostIds = cowalkPostList.stream()
                .map(CowalkPost::getId)
                .toList();

        Map<Long, Member> memberMap = memberRdbService.getMemberMap(memberIds);
        Map<Long, Integer> memberCountMap = cowalkParticipantRdbService.countByCowalkPostIds(cowalkPostIds);
        Map<Long, Integer> commentCountMap = cowalkCommentRdbService.countByCowalkPostIds(cowalkPostIds);
        List<CowalkPostsResponse> cowalkPostsResponseList = CowalkPostsResponse.from(
                cowalkPostList,
                memberMap,
                memberCountMap,
                commentCountMap
        );

        return new CursorResponse<>(cowalkPostsResponseList, cowalkPosts.hasNext());
    }

    @Transactional
    public Long saveCowalkComment(Long memberId, Long cowalkPostId,
                                  CreateCowalkCommentRequest createCowalkCommentRequest) {
        cowalkParticipantRdbService.validNotJoin(memberId, cowalkPostId);
        return cowalkCommentRdbService.save(memberId, cowalkPostId, createCowalkCommentRequest.content());
    }

    public CursorResponse<CowalkCommentResponse> getCowalkComments(Long cowalkPostId, Long crewId, Long memberId,
                                                                   Integer size, Long lastId) {
        crewMemberRdbService.validateIsCrewMember(crewId, memberId);

        CursorResponse<CowalkComment> cowalkComments
                = cowalkCommentRdbService.getCowalkComments(size, lastId, cowalkPostId);

        List<CowalkComment> cowalkCommentList = cowalkComments.data();

        List<Long> memberIds = cowalkCommentList.stream()
                .map(CowalkComment::getMemberId)
                .toList();

        Map<Long, Member> memberMap = memberRdbService.getMemberMap(memberIds);

        List<CowalkCommentResponse> responseList = CowalkCommentResponse.from(cowalkCommentList, memberMap);

        return new CursorResponse<>(responseList, cowalkComments.hasNext());
    }

    private LocalDate calculateEndDate(LocalDate startDate, LocalTime startTime, LocalTime endTime) {
        return startTime.isAfter(endTime) ? startDate.plusDays(1) : startDate;
    }
}
