package com.zq.dao;

import com.zq.bean.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDo record);

    int insertSelective(UserDo record);

    UserDo selectByPrimaryKey(Integer id);

    UserDo selectByEmail(String email);

    UserDo selectByName(String name);

    List<UserDo> selectAll();

    int updateByPrimaryKeySelective(UserDo record);

    int updateByPrimaryKey(UserDo record);

    int updataHeadImgById(@Param("id") Integer id,@Param("headImg") String headImg);
}