package member;

import com.dongsan.core.domains.member.Member;
import com.dongsan.core.domains.member.MemberRole;

public class MemberFixture {
    private static final Long ID = 1L;
    private static final String EMAIL = "abc@gmail.com";
    private static final String NICKNAME = "동네산책";
    private static final String PROFILE_IMAGE_URL = "image.png";
    private static final MemberRole ROLE = MemberRole.ROLE_USER;

    public static Member createMember(){
        return new Member(ID, EMAIL, NICKNAME, PROFILE_IMAGE_URL, ROLE);
    }

}
