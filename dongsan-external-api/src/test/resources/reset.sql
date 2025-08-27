-- 외래 키 제약 조건 비활성화
SET
FOREIGN_KEY_CHECKS = 0;

-- 모든 테이블 데이터 삭제
DELETE
FROM bookmark;
DELETE
FROM image;
DELETE
FROM liked_walkway;
DELETE
FROM marked_walkway;
DELETE
FROM member;
DELETE
FROM review;
DELETE
FROM walkway;
DELETE
FROM walkway_log;
DELETE
FROM cowalk_comment;
DELETE
FROM cowalk_participant;
DELETE
FROM cowalk_post;
DELETE
FROM crew;
DELETE
FROM crew_member;
DELETE
FROM meta_crew_ranking;
DELETE
FROM meta_walkway_liked;
DELETE
FROM meta_walkway_rating;

-- AUTO_INCREMENT 초기화
ALTER TABLE bookmark AUTO_INCREMENT = 1;
ALTER TABLE image AUTO_INCREMENT = 1;
ALTER TABLE liked_walkway AUTO_INCREMENT = 1;
ALTER TABLE marked_walkway AUTO_INCREMENT = 1;
ALTER TABLE member AUTO_INCREMENT = 1;
ALTER TABLE review AUTO_INCREMENT = 1;
ALTER TABLE walkway AUTO_INCREMENT = 1;
ALTER TABLE walkway_log AUTO_INCREMENT = 1;
ALTER TABLE cowalk_comment AUTO_INCREMENT = 1;
ALTER TABLE cowalk_participant AUTO_INCREMENT = 1;
ALTER TABLE cowalk_post AUTO_INCREMENT = 1;
ALTER TABLE crew AUTO_INCREMENT = 1;
ALTER TABLE crew_member AUTO_INCREMENT = 1;
ALTER TABLE meta_crew_ranking AUTO_INCREMENT = 1;
ALTER TABLE meta_walkway_liked AUTO_INCREMENT = 1;
ALTER TABLE meta_walkway_rating AUTO_INCREMENT = 1;


-- 외래 키 제약 조건 다시 활성화
SET
FOREIGN_KEY_CHECKS = 1;

-- 테스트용 Member 삽입
INSERT INTO member (created_at, email, nickname, provider, role)
VALUES (NOW(),
        'dongsan@example.com',
        '테스트유저',
        'KAKAO',
        'ROLE_USER');

INSERT INTO member (created_at, email, nickname, provider, role)
VALUES (NOW(),
        'ddongsan@example.com',
        '다른유저',
        'KAKAO',
        'ROLE_USER');
