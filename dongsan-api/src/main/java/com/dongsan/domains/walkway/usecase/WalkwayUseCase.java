package com.dongsan.domains.walkway.usecase;

import com.dongsan.common.annotation.UseCase;
import com.dongsan.domains.hashtag.entity.Hashtag;
import com.dongsan.domains.hashtag.entity.HashtagWalkway;
import com.dongsan.domains.hashtag.service.HashtagCommandService;
import com.dongsan.domains.hashtag.service.HashtagQueryService;
import com.dongsan.domains.hashtag.service.HashtagWalkwayCommandService;
import com.dongsan.domains.member.entity.Member;
import com.dongsan.domains.member.service.MemberQueryService;
import com.dongsan.domains.walkway.dto.request.CreateWalkwayRequest;
import com.dongsan.domains.walkway.dto.response.CreateWalkwayResponse;
import com.dongsan.domains.walkway.entity.Walkway;
import com.dongsan.domains.walkway.mapper.HashtagMapper;
import com.dongsan.domains.walkway.mapper.HashtagWalkwayMapper;
import com.dongsan.domains.walkway.mapper.WalkwayMapper;
import com.dongsan.domains.walkway.service.WalkwayCommandService;
import com.dongsan.error.code.MemberErrorCode;
import com.dongsan.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class WalkwayUseCase {

    private final WalkwayCommandService walkwayCommandService;

    private final MemberQueryService memberQueryService;

    private final HashtagWalkwayCommandService hashtagWalkwayCommandService;

    private final HashtagQueryService hashtagQueryService;

    private final HashtagCommandService hashtagCommandService;

    @Transactional
    public CreateWalkwayResponse createWalkway(CreateWalkwayRequest createWalkwayRequest, Long memberId) {

        Member member = memberQueryService.readMember(memberId)
                .orElseThrow(()-> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Walkway walkway = walkwayCommandService.createWalkway(WalkwayMapper.toWalkway(createWalkwayRequest, member));

        // 해쉬태그 추가
        if(!createWalkwayRequest.hashTags().isEmpty()) {
            createHashtagWalkways(walkway, createWalkwayRequest.hashTags());
        }

        // TODO: 경로 이미지 파일 저장

        return WalkwayMapper.toCreateWalkwayResponse(walkway);
    }

    @Transactional
    public List<HashtagWalkway> createHashtagWalkways(Walkway walkway, List<String> hashtagNames) {

        List<HashtagWalkway> hashtagWalkways = new ArrayList<>();
        List<Hashtag> hashtags = hashtagQueryService.getHashtagsByName(hashtagNames);

        for(String hashtagName: hashtagNames) {

            Optional<Hashtag> hashtagCheck = hashtags.stream()
                    .filter(hashtag -> hashtag.getName().equals(hashtagName))
                    .findFirst();

            HashtagWalkway hashtagWalkway;

            // 해쉬태그가 존재하지 않으면 생성 후 추가
            if(hashtagCheck.isPresent()) {
                hashtagWalkway = HashtagWalkwayMapper.toHashtagWalkway(hashtagCheck.get(), walkway);
            } else {
                Hashtag hashtag = HashtagMapper.toHashtag(hashtagName);
                hashtagCommandService.createHashtag(hashtag);
                hashtagWalkway = HashtagWalkwayMapper.toHashtagWalkway(hashtag, walkway);
            }
            hashtagWalkways.add(hashtagWalkway);
        }

        return hashtagWalkwayCommandService.createHashtagWalkways(hashtagWalkways);
    }
}
