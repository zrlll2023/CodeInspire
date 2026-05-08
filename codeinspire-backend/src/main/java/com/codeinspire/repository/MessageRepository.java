package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageRepository extends BaseMapper<Message> {
}
