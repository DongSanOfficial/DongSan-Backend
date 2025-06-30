package com.dongsan.domain.domains.image;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageJpaRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String url);
}
