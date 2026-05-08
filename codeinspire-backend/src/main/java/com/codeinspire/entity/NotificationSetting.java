package com.codeinspire.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notification_settings")
public class NotificationSetting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Boolean taskReminderEnabled;
    private Boolean timeNodeEnabled;
    private Boolean progressWarningEnabled;
    private Boolean aiReplyEnabled;
    private Boolean systemAnnouncementEnabled;
    private String quietStartHour;
    private String quietEndHour;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
