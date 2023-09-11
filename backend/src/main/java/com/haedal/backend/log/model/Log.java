package com.haedal.backend.log.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.haedal.backend.auth.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name="log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="log_type")
    private LogType logType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Column(name="log_time")
    private LocalDateTime logTime;
    @Column(name="log_desc")
    private String logDesc;

    @Builder
    public Log(User user, LogType logType, LocalDateTime logTime, String logDesc) {
        this.user = user;
        this.logType = logType;
        this.logTime = logTime;
        this.logDesc = logDesc;
    }
}
