package com.dongsan.domain.domains.walkway.infrastructure;

import com.dongsan.domain.domains.walkway.domain.MetaWalkwayLiked;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayLikedJpaRepository extends JpaRepository<MetaWalkwayLiked, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select mw from MetaWalkwayLiked mw where mw.walkwayId = :walkwayId")
    Optional<MetaWalkwayLiked> findByWalkwayIdForUpdate(@Param("walkwayId") Long walkwayId);
}
