package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.Conversation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConversationRepository extends BaseMapper<Conversation> {
}
