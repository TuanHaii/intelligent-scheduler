package com.example.intelligent_scheduler.domain.entity;

import com.example.intelligent_scheduler.domain.enums.ScheduleStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.SQLRestriction;
import java.time.OffsetDateTime;

@Entity
@Table(name = "schedules")
@SQLRestriction("deleted_at IS NULL")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1 user co the co nhieu schedule
     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id", nullable = false)
     private User user;

     // Lich nay giải quyết công việc nào nếu task_id null thì lịch này là lịch trống, có thể dùng để lên lịch cho công việc mới
     @OneToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "task_id")
     private Task task;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private OffsetDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    // JPA sẽ tự động kiểm tra version khi cập nhật để đảm bảo không có xung đột dữ liệu
    // ví dụ AI đang cập nhật cùng lúc với người dùng cập nhật, nếu version không khớp sẽ ném ra OptimisticLockException
    @Version
    private Long version;

    // sử dụng offsetDateTime để thực hiện logic kiểm tra trùng lịch
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
