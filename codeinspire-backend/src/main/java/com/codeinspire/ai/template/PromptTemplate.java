package com.codeinspire.ai.template;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("prompts")
public class PromptTemplate {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String scene;
    private String content;
    private String variables;
    private Integer version;
    private String status;
    private Boolean isAbTest;
    private String abGroup;
    private Integer usageCount;
    private Double satisfactionScore;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
