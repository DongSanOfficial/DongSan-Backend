package fixture;

import static com.dongsan.core.domains.member.MemberRole.ROLE_USER;

import com.dongsan.core.domains.auth.Provider;
import com.dongsan.rdb.domains.member.MemberEntity;

public class MemberEntityFixture {
    private static final String EMAIL = "abc@gmail.com";
    private static final String NICKNAME = "동네산책";
    private static final String PROFILE_IMAGE_URL = "image.png";
    private static final Provider PROVIDER = Provider.KAKAO;

    public static MemberEntity createMember(){
        return new MemberEntity(EMAIL, NICKNAME, PROFILE_IMAGE_URL, ROLE_USER, PROVIDER);
    }

    public static MemberEntity createMember(String email, String nickname, String profileImageUrl){
        return new MemberEntity(email, nickname, profileImageUrl, ROLE_USER, PROVIDER);
    }


}
