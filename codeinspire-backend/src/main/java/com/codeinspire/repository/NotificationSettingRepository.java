package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.NotificationSetting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationSettingRepository extends BaseMapper<NotificationSetting> {
}
