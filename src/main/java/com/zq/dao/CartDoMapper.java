package com.zq.dao;

import com.zq.bean.CartDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartDo record);

    int insertSelective(CartDo record);

    CartDo selectByPrimaryKey(Integer id);

    CartDo selectByUserIdAndPetId(@Param("userId") Integer userId,@Param("petId") Integer petId);

    List<CartDo> selectListByUserId(Integer userId);

    int updateByPrimaryKeySelective(CartDo record);

    int updateByPrimaryKey(CartDo record);

}