package com.zq.dao;

import com.zq.bean.PetImgDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PetImgDoMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByPetId(Integer petId);

    int insert(PetImgDo record);

    int insertSelective(PetImgDo record);

    PetImgDo selectByPrimaryKey(Integer id);

    List<PetImgDo> selectByPetId(Integer petId);

    int updateByPrimaryKeySelective(PetImgDo record);

    int updateByPrimaryKey(PetImgDo record);
}