package fixture;

import com.dongsan.rdb.domains.common.entity.BaseEntity;
import com.dongsan.rdb.domains.image.ImageEntity;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class ImageFixture {
    private static final String URL = "https://test.com";

    public static ImageEntity createImage(String url) {
        return new ImageEntity(url);
    }

    public static ImageEntity createImage() {
        return new ImageEntity(URL);
    }
}
