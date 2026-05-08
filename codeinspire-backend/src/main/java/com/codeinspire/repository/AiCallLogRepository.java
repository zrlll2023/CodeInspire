package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.AiCallLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AiCallLogRepository extends BaseMapper<AiCallLog> {
}
