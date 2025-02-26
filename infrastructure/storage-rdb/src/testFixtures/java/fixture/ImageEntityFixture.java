//package fixture;
//
//import com.dongsan.rdb.domains.common.entity.BaseEntity;
//import com.dongsan.rdb.domains.image.ImageEntity;
//import java.lang.reflect.Field;
//import java.time.LocalDateTime;
//
//public class ImageEntityFixture {
//    private static final String URL = "https://test.com";
//
//    public static ImageEntity createImage(String url) {
//        return ImageEntity.builder()
//                .url(url)
//                .build();
//    }
//
//    public static ImageEntity createImage() {
//        return ImageEntity.builder()
//                .url(URL)
//                .build();
//    }
//
//    public static ImageEntity createImageWithId(Long id, String url) {
//        ImageEntity imageEntity = ImageEntity.builder()
//                .url(url)
//                .build();
//        reflectId(id, imageEntity);
//        reflectCreatedAt(LocalDateTime.now(), imageEntity);
//        return imageEntity;
//    }
//
//    public static ImageEntity createImageWithId(Long id) {
//        ImageEntity imageEntity = ImageEntity.builder()
//                .url(URL)
//                .build();
//        reflectId(id, imageEntity);
//        reflectCreatedAt(LocalDateTime.now(), imageEntity);
//        return imageEntity;
//    }
//
//    private static void reflectId(Long id, ImageEntity imageEntity){
//        try {
//            Field idField = ImageEntity.class.getDeclaredField("id");
//            idField.setAccessible(true);
//            idField.set(imageEntity, id);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void reflectCreatedAt(LocalDateTime createdAt, ImageEntity imageEntity){
//        try {
//            Field createdAtField = BaseEntity.class.getDeclaredField("createdAt");
//            createdAtField.setAccessible(true);
//            createdAtField.set(imageEntity, createdAt);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}
