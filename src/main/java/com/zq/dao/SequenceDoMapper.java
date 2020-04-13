package com.zq.dao;

import com.zq.bean.SequenceDo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SequenceDoMapper {
    int deleteByPrimaryKey(String name);

    int insert(SequenceDo record);

    int insertSelective(SequenceDo record);

    SequenceDo selectByPrimaryKey(String name);

    SequenceDo getSequenceByName(String name);

    int updateByPrimaryKeySelective(SequenceDo record);

    int updateByPrimaryKey(SequenceDo record);
}