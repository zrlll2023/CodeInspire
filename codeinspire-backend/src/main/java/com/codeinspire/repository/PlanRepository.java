package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.Plan;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanRepository extends BaseMapper<Plan> {
}
