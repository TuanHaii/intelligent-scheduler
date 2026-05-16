package com.example.intelligent_scheduler.application.user.mapper;

import com.example.intelligent_scheduler.application.user.dto.UserProfileResponse;
import com.example.intelligent_scheduler.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // chuyen tu entity => DTO de tra ve cho client
    public UserProfileResponse toProfileResponse(User user) {
       if(user == null) return null;
       return new UserProfileResponse(
               user.getId(),
               user.getEmail(),
               user.getFullName(),
               user.getRole(),
               user.getWorkingHourStart(),
               user.getWorkingHourEnd(),
               user.getTimezone(),
               user.getIsActive()
       );
    }
}
