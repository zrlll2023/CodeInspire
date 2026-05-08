package com.codeinspire.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.codeinspire.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserRepository extends BaseMapper<User> {

    @Select("SELECT * FROM users WHERE username = #{username} OR email = #{email} LIMIT 1")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Select("SELECT * FROM users WHERE email = #{email} LIMIT 1")
    Optional<User> findByEmail(@Param("email") String email);
}
