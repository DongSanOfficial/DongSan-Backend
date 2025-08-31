package com.dongsan.domain.domains.walkway.infrastructure;

import com.dongsan.domain.domains.walkway.domain.MetaWalkwayRating;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaWalkwayRatingJpaRepository extends JpaRepository<MetaWalkwayRating, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select mw from MetaWalkwayRating mw where mw.walkwayId = :walkwayId")
    Optional<MetaWalkwayRating> findByWalkwayIdForUpdate(@Param("walkwayId") Long walkwayId);
}
