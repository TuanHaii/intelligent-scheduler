# Code Review Report — Intelligent Scheduler

**Reviewer:** Senior Tester (10+ years experience)
**Date:** 2026-05-16
**Project:** intelligent-scheduler (Spring Boot 4.0.6, Java 21)

---

## 🔴 MỨC ĐỘ CRITICAL

### 1. `application.properties` sai định dạng — không hoạt động

- **File:** `src/main/resources/application.properties:2-9`

```properties
app:
    jwt:
    secret: ${JWT_SECRET:8hrxmh/...}
    expiration-seconds: 900
```

Sử dụng cú pháp YAML (indentation + colon) trong file `.properties`. Spring Boot không parse được nested properties theo cách này.

**Hậu quả:**
- `app.jwt.secret`, `app.jwt.expiration-seconds`, `app.jwt.refresh-expiration-seconds`, `app.openai.api-key` không được đọc → tất cả fallback về default trong code Java
- Default trong code (`JwtConfig.java:18`: `default-secret-change-me...`) khác với default trong properties (`8hrxmh/...`) → gây nhầm lẫn

**Fix:** Đổi sang format `.properties` hoặc rename file thành `application.yml`.

---

### 2. `TaskController.java` — Path variable mismatch (`updateTask`)

- **File:** `src/main/java/.../presentation/controller/TaskController.java:47`

```java
@PutMapping("/{taskId}")
public ResponseEntity<TaskResponse> updateTask(
    @AuthenticationPrincipal Jwt jwt,
    @PathVariable("id") Long taskid,  // <-- sai!
    ...
) {
```

`@PathVariable("id")` không khớp với `{taskId}` trong URL mapping. Spring sẽ không bind được → API luôn trả về 400 Bad Request hoặc `null`.

**Fix:** `@PathVariable("taskId")` hoặc đổi URL thành `/{id}`.

---

### 3. `UserMapper.java` — Mapping sai field

- **File:** `src/main/java/.../application/user/mapper/UserMapper.java:17`

```java
return new UserProfileResponse(
    ...
    user.getIsActive()       // <-- sai: gán isActive vào isEmailVerified
);
```

Trường `isEmailVerified` trong response nhận giá trị từ `isActive` thay vì `isEmailVerified`. Client nhận sai dữ liệu.

**Fix:** Dùng `user.getIsEmailVerified()`.

---

### 4. `UserServiceImplTest.java:76` — Assert sai data

- **File:** `src/test/java/.../application/user/service/impl/UserServiceImplTest.java:76`

```java
assertEquals("Test User", actualResult.email());
```

Mock response được tạo với email `"hai@gmail.com"` (dòng 62), nhưng assert là `"Test User"`. Test luôn FAIL.

**Fix:** `assertEquals("hai@gmail.com", actualResult.email())`.

---

### 5. `UserServiceImplTest.java:99` — Assert sai exception message

- **File:** `src/test/java/.../application/user/service/impl/UserServiceImplTest.java:99`

```java
assertEquals("Không tìm thấy người dùng trong hệ thống", exception.getMessage());
```

Code thật (`UserServiceImpl.java:23`) throw `"User không tồn tại"`. Test luôn FAIL.

**Fix:** `assertEquals("User không tồn tại", exception.getMessage())`.

---

## 🟠 MỨC ĐỘ HIGH

### 6. JWT secret có default value hardcode

- **File:** `src/main/java/.../infrastructure/security/JwtConfig.java:18`
- **File:** `src/main/resources/application.properties:5`

Cả code lẫn properties đều chứa secret mặc định. Nếu quên set biến môi trường `JWT_SECRET`, attacker có thể forge JWT token.

**Recommendation:**
- Không có default value — để trống và crash sớm nếu thiếu
- Hoặc dùng giá trị random khi dev, document rõ ràng

---

### 7. CSRF + CORS tắt hoàn toàn

- **File:** `src/main/java/.../infrastructure/security/SecurityConfig.java:26-27`

```java
.csrf(AbstractHttpConfigurer::disable)
.cors(AbstractHttpConfigurer::disable)
```

Không có CORS policy → API có thể bị gọi từ bất kỳ domain nào. Nếu dùng cookie-based auth, CSRF disable là lỗ hổng.

**Recommendation:** Cấu hình CORS whitelist cụ thể. Nếu dùng JWT Bearer thì CSRF disable là chấp nhận được, nhưng CORS cần config.

---

### 8. IDOR — Information Disclosure qua error message

- **File:** `src/main/java/.../application/task/service/impl/TaskServiceImpl.java:96-98`

```java
orElseThrow(() -> new IllegalArgumentException("Không tìm thấy task"));   // task không tồn tại
...
throw new IllegalArgumentException("Không tìm thấy công việc");              // task không thuộc user
```

Attacker có thể brute-force `taskId` và phân biệt "task có tồn tại nhưng không thuộc user" vs "task không tồn tại".

**Fix:** Dùng chung một message generic: `"Task không tìm thấy hoặc không thuộc về bạn"`.

---

## 🟡 MỨC ĐỘ MEDIUM

### 9. Thiếu gần như toàn bộ tests

Chỉ có **1 unit test class thật** (`UserServiceImplTest`) và `contextLoads`. Còn lại là code duplicate từ `src/main`.

**Thiếu test cho:**
| Component | Loại test cần |
|-----------|---------------|
| `AuthController`, `TaskController`, `UserController` | Integration / WebMvcTest |
| `AuthServiceImpl` | Unit test |
| `TaskServiceImpl` | Unit test |
| `jwtProvider` | Unit test |
| `SecurityConfig` | Security test |
| `GlobalExceptionHandler` | Unit test |
| `TaskRepository`, `UserRepository` | DataJpaTest |
| `TaskMapper`, `UserMapper` | Unit test |

---

### 10. EmailServiceImpl — `@Async` missing

- **File:** `src/main/java/.../infrastructure/external/EmailServiceImpl.java`

```java
public void sendWelcomeEmailAsync(String email, String fullName) {
```

Method có `Async` trong tên nhưng không có `@Async` annotation. `Thread.sleep(3000)` chạy synchronous, block request thread.

---

### 11. Soft-delete không nhất quán

- **File:** `src/main/java/.../repository/TaskRepository.java`

```java
Optional<Task> findTaskById(UUID id);  // không filter deletedAt
List<Task> findByUserIdAndStatusAndDeletedAtIsNull(UUID userId, TaskStatus status);
List<Task> findByUserIdAndDeletedAtIsNull(UUID userId);
```

`findTaskById` có thể trả về task đã xóa.

**Recommendation:** Dùng `@Where(clause = "deleted_at IS NULL")` trên entity hoặc đảm bảo mọi query đều filter.

---

### 12. PasswordEncoder khởi tạo field thay vì inject bean

- **File:** `src/main/java/.../application/auth/service/impl/AuthServiceImpl.java:28`

```java
private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
```

Tạo instance mới thay vì dùng bean từ `SecurityConfig`. Lãng phí, không nhất quán, khó test.

---

### 13. `getReferenceById` — Lazy loading exception

- **File:** `src/main/java/.../application/task/service/impl/TaskServiceImpl.java:38`

```java
User userProxy = userRepository.getReferenceById(userId);
```

Nếu `userId` không tồn tại, `EntityNotFoundException` chỉ ném ra lúc `flush()` (commit), không phải lúc gọi. Khó debug và track nguyên nhân.

**Fix:** Dùng `findById()` + throw exception rõ ràng.

---

### 14. RefreshToken column length không đủ

- **File:** `src/main/java/.../domain/entity/RefreshToken.java:35`

```java
@Column(nullable = false, unique = true, length = 500)
private String token;
```

JWT token dễ dàng vượt quá 500 ký tự. Dùng `@Column(columnDefinition = "TEXT")` hoặc `length = 2048`.

---

### 15. Role là String — không type-safe

- **File:** `src/main/java/.../domain/entity/User.java:26`

```java
private String role;
```

Dùng String cho role dễ gây lỗi chính tả. Nên dùng enum (`USER`, `ADMIN`).

---

## 🔵 MỨC ĐỘ LOW

### 16. `System.out.println` thay vì Logger

- **File:** `src/main/java/.../application/auth/service/impl/AuthServiceImpl.java:49-50`

```java
System.out.println("Đã lưu Refresh Token mới cho user: {} " + user.getEmail());
System.out.println("User {} đã đăng nhập thành công " + user.getEmail());
```

Dùng `System.out` không ghi được level, timestamp, không config được. Trong cùng file có dùng Slf4j cho log khác → không nhất quán.

---

### 17. `Thread.sleep(1000)` vô dụng trong ActivityLogService

- **File:** `src/main/java/.../application/common/service/ActivityLogService.java:16`

```java
Thread.sleep(1000);
```

Không có business logic gì — chỉ làm chậm 1s vô ích. Là method mock còn sót lại?

---

### 18. Class naming: `jwtProvider`

- **File:** `src/main/java/.../infrastructure/security/jwtProvider.java`

Java convention: class nên là `JwtProvider` (viết hoa chữ J). Hiện tại import ở `AuthServiceImpl.java:18` nhìn thiếu chuyên nghiệp.

---

### 19. Tồn tại nhiều file .java duplicate trong thư mục test

`src/test/java` chứa bản sao của toàn bộ DTO, Entity, Service từ `src/main/java`. Đây không phải test — có thể do copy nhầm. Gây nhầm lẫn, tăng kích thước build.

---

## 📋 TỔNG KẾT

| Mức độ | Số lượng | Mô tả |
|--------|:--------:|-------|
| 🔴 Critical | 5 | App không chạy đúng, API không hoạt động, test fail |
| 🟠 High | 3 | Lỗ hổng bảo mật, lộ thông tin |
| 🟡 Medium | 7 | Thiết kế chưa tốt, thiếu test |
| 🔵 Low | 4 | Coding convention, code thừa |

**Điểm mạnh:**
- Cấu trúc package clean (controller → service → repository)
- Dùng record cho DTO (immutable)
- Soft-delete hỗ trợ khôi phục dữ liệu
- Optimistic locking (`@Version`) trên entity
- Global exception handler tập trung
- Phân tách interface/impl

**Điểm yếu chính:**
1. `application.properties` sai format → toàn bộ cấu hình application không hoạt động
2. `TaskController` path variable mismatch → 2 API bị hỏng
3. Tests không chạy được do assert sai
4. Bảo mật JWT có default secret hardcode
5. Code test thư mục bị nhiễm bản sao source code
