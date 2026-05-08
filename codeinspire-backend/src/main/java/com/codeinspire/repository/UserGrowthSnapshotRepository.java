package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.UserGrowthSnapshot;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserGrowthSnapshotRepository extends BaseMapper<UserGrowthSnapshot> {
}
