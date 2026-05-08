package com.codeinspire.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("skill_assessments")
public class SkillAssessment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String skillCategory;
    private String skillName;
    private Integer level;
    private String assessmentMethod;
    private String evidence;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime assessedAt;
}
