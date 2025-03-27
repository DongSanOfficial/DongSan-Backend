package image;

import com.dongsan.core.domains.image.Image;

public class ImageFixture {
	private static final Long ID = 1L;
	private static final String IMAGE_URL = "TEST URL";

	public static Image createImage() {
		return new Image(ID, IMAGE_URL);
	}
}
