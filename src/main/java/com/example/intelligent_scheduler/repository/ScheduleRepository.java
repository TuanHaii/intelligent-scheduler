package com.example.intelligent_scheduler.repository;

import com.example.intelligent_scheduler.domain.entity.Schedule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // lay danh sach lich trong 1 khoang thoi gian
    List<Schedule> findByUserIdAndStartTimeBetweenOrderByStartTimeAsc(UUID userId, OffsetDateTime start, OffsetDateTime end);


    // Thuat toan check trung lich
    // tra ve true neu co lich trung
    @Query("""
    SELECT CASE WHEN COUNT(s) > 0  THEN true ELSE false END
    FROM Schedule s 
    WHERE s.user.id = :userId
        AND s.startTime < :newEnd
        AND s.endTime > :newStart
    """)
    boolean hasConflict(
            @Param("userId") UUID userId,
            @Param("newStart") OffsetDateTime newStart,
            @Param("newEnd") OffsetDateTime newEnd
    );
}
