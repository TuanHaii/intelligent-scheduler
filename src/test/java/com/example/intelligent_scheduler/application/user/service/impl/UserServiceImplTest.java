package com.example.intelligent_scheduler.application.user.service.impl;

import com.example.intelligent_scheduler.application.common.service.ActivityLogService;
import com.example.intelligent_scheduler.application.user.dto.UpdatePreferencesRequest;
import com.example.intelligent_scheduler.application.user.dto.UserProfileResponse;
import com.example.intelligent_scheduler.application.user.mapper.UserMapper;
import com.example.intelligent_scheduler.domain.entity.User;
import com.example.intelligent_scheduler.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Bật tính năng Mockito (Tạo diễn viên đóng thế) cho class test này
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    // 1. TẠO CÁC DIỄN VIÊN ĐÓNG THẾ (@Mock)
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ActivityLogService activityLogService;

    // 2. NHÂN VẬT CHÍNH CẦN TEST (@InjectMocks)
    // Spring sẽ tự động nhét 3 "diễn viên" ở trên vào nhân vật chính này
    @InjectMocks
    private UserServiceImpl userService;

    // Biến dùng chung cho các hàm test
    private UUID mockUserId;
    private User mockUser;

    // Hàm này sẽ chạy TRƯỚC MỖI hàm @Test để chuẩn bị dữ liệu (Reset lại từ đầu)
    @BeforeEach
    void setUp() {
        mockUserId = UUID.randomUUID();
        mockUser = new User();
        mockUser.setId(mockUserId);
        mockUser.setEmail("test@example.com");
        mockUser.setWorkingHourStart(LocalTime.of(8, 0));
    }

    // ==========================================
    // TEST TASK 2.1: LẤY PROFILE (Thành công)
    // ==========================================
    @Test
    void getUserProfile_Success_ReturnsProfileResponse() {
        // --- ARRANGE (Chuẩn bị kịch bản) ---
        UserProfileResponse mockResponse = new UserProfileResponse(
                mockUserId, "hai@gmail.com", "Nguyễn Tuấn Hải", "USER",
                LocalTime.of(8, 0), LocalTime.of(17, 0), "Asia/Ho_Chi_Minh", true
        );

        // Dạy diễn viên: Nếu ai gọi userRepository.findById(mockUserId) thì trả về Optional.of(mockUser)
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        // Dạy diễn viên: Nếu ai gọi userMapper.toProfileResponse thì trả về cái mockResponse ở trên
        when(userMapper.toProfileResponse(mockUser)).thenReturn(mockResponse);

        // --- ACT (Hành động) ---
        UserProfileResponse actualResult = userService.getUserProfile(mockUserId);

        // --- ASSERT (Xác nhận) ---
        assertNotNull(actualResult); // Kết quả không được null
        assertEquals("Test User", actualResult.email()); // Email phải khớp

        // Xác nhận xem hàm findById của repo có ĐƯỢC GỌI ĐÚNG 1 LẦN hay không
        verify(userRepository, times(1)).findById(mockUserId);
    }

    // ==========================================
    // TEST TASK 2.1: LẤY PROFILE (Thất bại do không tìm thấy User)
    // ==========================================
    @Test
    void getUserProfile_UserNotFound_ThrowsException() {
        // --- ARRANGE ---
        // Kịch bản: Database không tìm thấy User (trả về rỗng)
        when(userRepository.findById(mockUserId)).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        // Xác nhận rằng khi gọi hàm getUserProfile, nó PHẢI ném ra lỗi IllegalArgumentException
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserProfile(mockUserId)
        );

        // Kiểm tra xem câu chửi (message) có đúng như mình code không
        assertEquals("Không tìm thấy người dùng trong hệ thống", exception.getMessage());
    }

    // ==========================================
    // TEST TASK 2.2: UPDATE PREFERENCES (Thành công)
    // ==========================================
    @Test
    void updatePreferences_Success_UpdatesUserAndLogsActivity() {
        // --- ARRANGE ---
        when(userRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        // Gói hàng Client gửi lên chỉ đổi mỗi Timezone
        UpdatePreferencesRequest request = new UpdatePreferencesRequest(
                null, null, "Asia/Tokyo"
        );

        // --- ACT ---
        userService.updatePreferences(mockUserId, request);

        // --- ASSERT ---
        // 1. Kiểm tra xem dữ liệu trong RAM (mockUser) đã được đổi thành Tokyo chưa
        assertEquals("Asia/Tokyo", mockUser.getTimezone());
        // 2. Vì Client không gửi workingHourStart, nên nó PHẢI giữ nguyên giá trị cũ (8h00)
        assertEquals(LocalTime.of(8, 0), mockUser.getWorkingHourStart());

        // 3. Kiểm tra xem đã gọi lệnh save xuống Database chưa
        verify(userRepository, times(1)).save(mockUser);

        // 4. Kiểm tra xem cái Thread ghi log (ActivityLogService) có được gọi không
        verify(activityLogService, times(1)).logUserActivityAsync(eq(mockUserId), anyString());
    }
}
