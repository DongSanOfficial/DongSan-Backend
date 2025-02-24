package fixture;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.walkway.repository.HashtagEntity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class HashtagEntityFixture {

    private static final String NAME = "산책로 태그";

    public static HashtagEntity createHashtag(){
        return HashtagEntity.builder()
                .name(NAME)
                .build();
    }

    public static HashtagEntity createHashtag(String name){
        return HashtagEntity.builder()
                .name(name)
                .build();
    }

    public static HashtagEntity createHashtagWithId(Long id){
        HashtagEntity hashtagEntity = createHashtag();
        reflectId(id, hashtagEntity);
        reflectCreatedAt(LocalDateTime.now(), hashtagEntity);
        return hashtagEntity;
    }

    public static HashtagEntity createHashtagWithId(Long id, String name){
        HashtagEntity hashtagEntity = createHashtag(name);
        reflectId(id, hashtagEntity);
        reflectCreatedAt(LocalDateTime.now(), hashtagEntity);
        return hashtagEntity;
    }

    private static void reflectId(Long id, HashtagEntity hashtagEntity){
        try {
            Field idField = HashtagEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(hashtagEntity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, HashtagEntity hashtagEntity){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(hashtagEntity, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
