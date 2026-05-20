# mallegan-ads-lib (bản trong repo)

Đây là mã nguồn trích từ AAR **`ads-mallegan-lib`** `1.5.3`, đặt vào module `mallegan-ads-lib` để bạn tự chỉnh sửa và **publish Maven** (`maven-publish` đã được cấu hình).

## Kiến trúc

- `src/main/java/com/mallegan/ads/**` — Java/Kotlin từ `*-sources.jar`
- `src/main/res/**`, `AndroidManifest.xml` — từ AAR  
- **`namespace` Gradle:** `com.mallegan.ads`

## Publish AAR vào Maven (local hoặc thư mục)

Đảm bảo máy **JDK 17+** (AGP `8.7.x` không chạy với Java 8).

Đăng nhập group / artifact / version (tùy chọn trong `gradle.properties` ở root project):

```properties
malleganAdsLibGroupId=com.company.yourteam
malleganAdsLibArtifactId=your-ads-sdk
malleganAdsLibVersion=1.0.0
# folder nhận repo (mặc định: mallegan-ads-lib/build/repo)
malleganAdsLibDeployRepo=file:///C:/maven/repo
```

Để đẩy lên **Maven Local** (`~/.m2/repository`):

```bash
./gradlew :mallegan-ads-lib:publishMalleganAdsReleasePublicationToMavenLocal
```

Để đẩy vào **`malleganAdsLibDeployRepo`** và **Maven Local** cùng lúc:

```bash
./gradlew :mallegan-ads-lib:publish
```

## App đang phụ thuộc module

Trong `app/build.gradle`, dependency **`com.github.nguyentruongnvt72:ads-mallegan-lib`** đã đổi thành:

```gradle
implementation project(':mallegan-ads-lib')
```

App vẫn cần `google-services.json`, plugin Firebase trên `:app`, v.v.—giống trước.

## Giấy phép & đạo đức publish

Đây là mã của bên thứ ba. Trước khi tái đóng gói dưới groupId/artifactId của bạn, hãy kiểm tra **license** của repo gốc và điều khoản JitPack/GitHub; mình không cung cấp tư vấn pháp lý.
