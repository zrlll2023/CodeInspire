package com.codeinspire.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("user_growth_snapshots")
public class UserGrowthSnapshot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate snapshotDate;
    private String skillSummary;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
