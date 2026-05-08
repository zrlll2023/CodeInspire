package com.codeinspire.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_feedbacks")
public class UserFeedback {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long messageId;
    private String type;
    private Integer rating;
    private String comment;
    private String feedbackDetails;
    private String status;
    private String adminReply;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
