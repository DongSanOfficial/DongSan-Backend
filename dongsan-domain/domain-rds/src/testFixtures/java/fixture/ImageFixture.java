package fixture;

import com.dongsan.rdb.domains.image.ImageEntity;

public class ImageFixture {
	private static final String URL = "https://test.com";

	public static ImageEntity createImage(String url) {
		return new ImageEntity(url);
	}

	public static ImageEntity createImage() {
		return new ImageEntity(URL);
	}
}
