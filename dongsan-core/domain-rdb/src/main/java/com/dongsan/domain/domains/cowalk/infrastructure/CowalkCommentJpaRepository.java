package com.dongsan.domain.domains.cowalk.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dongsan.domain.domains.cowalk.domain.CowalkComment;

@Repository
public interface CowalkCommentJpaRepository extends JpaRepository<CowalkComment, Long> {
	Integer countByCowalkPostId(Long cowalkPostId);
}
