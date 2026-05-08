package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.TaskExecutionLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskExecutionLogRepository extends BaseMapper<TaskExecutionLog> {
}
