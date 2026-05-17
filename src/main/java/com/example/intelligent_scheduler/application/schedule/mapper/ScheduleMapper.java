package com.example.intelligent_scheduler.application.schedule.mapper;

import com.example.intelligent_scheduler.application.schedule.dto.ScheduleResponse;
import com.example.intelligent_scheduler.domain.entity.Schedule;
import org.springframework.stereotype.Component;

@Component
// lay du lieu tu entity schedule va cùnhg lúc đó lấy title của task để trả về cho client
public class ScheduleMapper {
    public ScheduleResponse toScheduleResponse(Schedule schedule) {
        if(schedule == null) return null;
        // neu schedule co gan voi task thi lay title cua task, neu khong co thi de null
        String title = (schedule.getTask() != null) ? schedule.getTask().getTitle() : "Sự kiện tự do";
        Long taskId = (schedule.getTask() != null) ? schedule.getTask().getId() : null;
        return new ScheduleResponse(
                schedule.getId(),
                taskId,
                title,
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}
