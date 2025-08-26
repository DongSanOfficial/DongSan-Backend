package com.dongsan.api.domains.cowalk;

import org.springframework.stereotype.Service;

import com.dongsan.api.domains.cowalk.dto.response.GetMyCowalkResponse;
import com.dongsan.domain.domains.cowalk.domain.CowalkPost;
import com.dongsan.domain.domains.cowalk.service.CowalkPostRdbService;
import com.dongsan.domain.support.paging.CursorRequest;
import com.dongsan.domain.support.paging.CursorResponse;

@Service
public class MyCowalkFacade {
    private final CowalkPostRdbService cowalkPostRdbService;

    public MyCowalkFacade(CowalkPostRdbService cowalkPostRdbService) {
        this.cowalkPostRdbService = cowalkPostRdbService;
    }

    public CursorResponse<GetMyCowalkResponse> getMyCowalk(CursorRequest cursorRequest, Long memberId) {
        CursorResponse<CowalkPost> result = cowalkPostRdbService.getJoinedCowalkPost(memberId, cursorRequest.lastId(), cursorRequest.size());
        return new CursorResponse<>(GetMyCowalkResponse.from(result.data()), result.hasNext());
    }
}
