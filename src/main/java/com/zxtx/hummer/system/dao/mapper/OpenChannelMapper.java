package com.zxtx.hummer.system.dao.mapper;

import com.zxtx.hummer.system.domain.OpenChannel;
import com.zxtx.hummer.system.domain.OpenChannelExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface OpenChannelMapper {
    long countByExample(OpenChannelExample example);

    int deleteByExample(OpenChannelExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OpenChannel record);

    int insertSelective(OpenChannel record);

    List<OpenChannel> selectByExampleWithRowbounds(OpenChannelExample example, RowBounds rowBounds);

    List<OpenChannel> selectByExample(OpenChannelExample example);

    OpenChannel selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OpenChannel record, @Param("example") OpenChannelExample example);

    int updateByExample(@Param("record") OpenChannel record, @Param("example") OpenChannelExample example);

    int updateByPrimaryKeySelective(OpenChannel record);

    int updateByPrimaryKey(OpenChannel record);
}