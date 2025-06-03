package com.dongsan.api.domains.cowalk;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongsan.api.domains.cowalk.dto.request.CreateCowalkCommentRequest;
import com.dongsan.api.domains.cowalk.dto.request.CreateCowalkPostRequest;
import com.dongsan.api.domains.cowalk.dto.response.CowalkPostDetailResponse;
import com.dongsan.api.domains.cowalk.dto.response.CowalkPostsResponse;
import com.dongsan.domain.domains.cowalk.CowalkPostLockService;
import com.dongsan.domain.domains.cowalk.CreateCowalkPostCommand;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.service.CowalkCommentRdbService;
import com.dongsan.domain.domains.cowalk.service.CowalkParticipantRdbService;
import com.dongsan.domain.domains.cowalk.service.CowalkPostRdbService;
import com.dongsan.domain.domains.crew.service.CrewMemberRdbService;
import com.dongsan.domain.domains.member.Member;
import com.dongsan.domain.domains.member.MemberRdbService;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
            MemberRdbService memberRdbService, CowalkPostLockService cowalkPostLockService
    ) {
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
        CreateCowalkPostCommand command = createCowalkPostRequest.toCreateCowalkPostCommand(crewId, memberId);
        Long cowalkPostId = cowalkPostRdbService.save(command);
        cowalkParticipantRdbService.save(memberId, cowalkPostId);
        return cowalkPostId;
    }

    public CowalkPostDetailResponse getCowalkPostDetail(Long cowalkPostId, Long crewId, Long memberId) {
        crewMemberRdbService.validateIsCrewMember(crewId, memberId);
        CowalkPost cowalkPost = cowalkPostRdbService.getCowalkPost(cowalkPostId);
        Integer participantCount = cowalkParticipantRdbService.countByCowalkPostId(cowalkPostId);
        Integer commentCount = cowalkCommentRdbService.countByCowalkPostId(cowalkPostId);
        Member member = memberRdbService.getMember(memberId);
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
        List<CowalkPost> cowalkPostList = cowalkPosts.getData();

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

        return new CursorResponse<>(cowalkPostsResponseList, cowalkPosts.getHasNext());
    }

    public Long saveCowalkComment(Long memberId, Long cowalkPostId,
            CreateCowalkCommentRequest createCowalkCommentRequest) {
        cowalkParticipantRdbService.validNotJoin(cowalkPostId, memberId);
        return cowalkCommentRdbService.save(memberId, cowalkPostId, createCowalkCommentRequest.content());
    }
}
