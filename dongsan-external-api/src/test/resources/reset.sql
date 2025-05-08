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
FROM walkway_history;

-- AUTO_INCREMENT 초기화
ALTER TABLE bookmark AUTO_INCREMENT = 1;
ALTER TABLE image AUTO_INCREMENT = 1;
ALTER TABLE liked_walkway AUTO_INCREMENT = 1;
ALTER TABLE marked_walkway AUTO_INCREMENT = 1;
ALTER TABLE member AUTO_INCREMENT = 1;
ALTER TABLE review AUTO_INCREMENT = 1;
ALTER TABLE walkway AUTO_INCREMENT = 1;
ALTER TABLE walkway_history AUTO_INCREMENT = 1;

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
