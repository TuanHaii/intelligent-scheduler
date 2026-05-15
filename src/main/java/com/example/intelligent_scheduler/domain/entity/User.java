package com.example.intelligent_scheduler.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

// entity user sẽ ánh xạ
@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column( unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name")
    private String fullName;

    private String role;

    // time bat dau
    @Column(name = "working_hour_start")
    private LocalTime workingHourStart;

    // time ket thuc
    @Column(name = "working_hour_end")
    private LocalTime workingHourEnd;
}
