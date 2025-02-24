package fixture;

import static com.dongsan.core.domains.member.MemberRole.*;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.member.MemberEntity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class MemberEntityFixture {
    private static final String EMAIL = "abc@gmail.com";
    private static final String NICKNAME = "동네산책";
    private static final String PROFILE_IMAGE_URL = "image.png";

    public static MemberEntity createMember(){
        return new MemberEntity(EMAIL, NICKNAME, PROFILE_IMAGE_URL, ROLE_USER);
    }

    public static MemberEntity createMember(String email, String nickname, String profileImageUrl){
        return new MemberEntity(email, nickname, profileImageUrl, ROLE_USER);
    }

    public static MemberEntity createMemberWithId(Long id){
        MemberEntity memberEntity = createMember();
        reflectId(id, memberEntity);
        reflectCreatedAt(LocalDateTime.now(), memberEntity);
        return memberEntity;
    }

    public static MemberEntity createMemberWithId(Long id, String email, String nickname, String profileImageUrl){
        MemberEntity memberEntity = createMember(email, nickname, profileImageUrl);
        reflectId(id, memberEntity);
        reflectCreatedAt(LocalDateTime.now(), memberEntity);
        return memberEntity;
    }

    private static void reflectId(Long id, MemberEntity memberEntity){
        try {
            Field idField = MemberEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(memberEntity, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void reflectCreatedAt(LocalDateTime createdAt, MemberEntity memberEntity){
        try {
            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(memberEntity, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
