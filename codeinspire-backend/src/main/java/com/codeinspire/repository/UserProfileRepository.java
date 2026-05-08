package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileRepository extends BaseMapper<UserProfile> {
}
