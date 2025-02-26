//package com.dongsan.domains.hashtag.repository;
//
//import com.dongsan.common.support.RepositoryTest;
//import com.dongsan.rdb.domains.walkway.repository.HashtagDSLRepository;
//import com.dongsan.rdb.domains.walkway.repository.HashtagEntity;
//import com.dongsan.rdb.domains.member.MemberEntity;
//import com.dongsan.rdb.domains.walkway.WalkwayEntity;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.List;
//
//import static fixture.HashtagFixture.createHashtag;
//import static fixture.HashtagWalkwayFixture.createHashtagWalkway;
//import static fixture.MemberFixture.createMember;
//import static fixture.WalkwayFixture.createWalkway;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DisplayName("HashtagDSLRepository Unit Test")
//class HashtagEntityDSLRepositoryTest extends RepositoryTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private HashtagDSLRepository hashtagDSLRepository;
//
//    @Nested
//    @DisplayName("getHashtagsByWalkwayId 메서드는")
//    class Describe_getHashtagsByWalkwayIdEntity {
//
//        @Test
//        @DisplayName("walkwayId에 해당 하는 해쉬태그를 불러온다.")
//        void it_returns_hashtags() {
//            // given
//            MemberEntity memberEntity = createMember();
//            WalkwayEntity walkwayEntity = createWalkway(memberEntity);
//            entityManager.persist(memberEntity);
//            entityManager.persist(walkwayEntity);
//            for(Long i = 0L; i < 3L; i++) {
//                HashtagEntity hashtagEntity = createHashtag("test"+i);
//                entityManager.persist(hashtagEntity);
//                entityManager.persist(createHashtagWalkway(walkwayEntity, hashtagEntity));
//            }
//
//            // when
//            List<HashtagEntity> result = hashtagDSLRepository.getHashtagsByWalkwayId(walkwayEntity.getId());
//
//            // then
//            assertThat(result.size()).isEqualTo(3);
//            for(int i = 0; i < 3; i++) {
//                assertThat(result.get(i).getName()).isEqualTo("test"+i);
//            }
//        }
//    }
//
//}