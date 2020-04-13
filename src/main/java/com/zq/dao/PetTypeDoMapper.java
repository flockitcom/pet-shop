package com.zq.dao;

import com.zq.bean.PetTypeDo;

import java.util.List;

public interface PetTypeDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PetTypeDo record);

    int insertSelective(PetTypeDo record);

    PetTypeDo selectByPrimaryKey(Integer id);

    List<PetTypeDo> selectAll();

    int updateByPrimaryKeySelective(PetTypeDo record);

    int updateByPrimaryKey(PetTypeDo record);
}