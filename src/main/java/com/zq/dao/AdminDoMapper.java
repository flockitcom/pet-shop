package com.zq.dao;

import com.zq.bean.AdminDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminDoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByName(String name);

    int insert(AdminDo record);

    int insertSelective(AdminDo record);

    AdminDo selectByPrimaryKey(Integer id);

    AdminDo selectByName(String name);

    AdminDo selectByIdAndPassword(@Param(value = "id") Integer id, @Param(value = "password")String password);

    List<AdminDo> selectAll();

    int updateByPrimaryKeySelective(AdminDo record);

    int updateByPrimaryKey(AdminDo record);
}