package fixture;

import com.dongsan.core.domains.image.Image;
import com.dongsan.core.domains.walkway.ExposeLevel;
import com.dongsan.core.domains.walkway.Walkway;
import com.dongsan.core.support.util.Author;

public class ImageFixture {
    private static final Long ID = 1L;
    private static final String IMAGE_URL = "TEST URL";

    public static Image createImage() {
        return new Image(ID, IMAGE_URL);
    }
}
