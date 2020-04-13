package com.zq.dao;

import com.zq.bean.PetDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PetDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PetDo record);

    int insertSelective(PetDo record);

    PetDo selectByPrimaryKey(Integer id);

    List<PetDo> selectPetListByTypeId(Integer typeId);

    List<PetDo> selectPetListByKeyword(String keyword);

    List<PetDo> selectPetList();

    int updateByPrimaryKeySelective(PetDo record);

    int updateByPrimaryKey(PetDo record);

    int decreaseStock(@Param("petId") Integer petId, @Param("amount") Integer amount);

    int increaseSales(@Param("petId") Integer petId, @Param("amount") Integer amount);
}