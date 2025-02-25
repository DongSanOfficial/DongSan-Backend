package com.dongsan.rdb.domains.walkway;

import com.dongsan.rdb.domains.member.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikedWalkwayJpaRepository extends JpaRepository<LikedWalkwayEntity, Long> {

    void deleteByMemberEntityIdAndWalkwayEntityId(Long memberId, Long walkwayId);

    Boolean existsByMemberEntityIdAndWalkwayEntityId(Long memberId, Long walkwayId);
}
