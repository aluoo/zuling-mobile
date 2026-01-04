package com.zxtx.hummer.em.dao.mapper;


import com.zxtx.hummer.em.domain.EmployeeHistory;
import com.zxtx.hummer.em.domain.EmployeeHistoryExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface EmployeeHistoryMapper {
    long countByExample(EmployeeHistoryExample example);

    int deleteByExample(EmployeeHistoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(EmployeeHistory record);

    int insertSelective(EmployeeHistory record);

    List<EmployeeHistory> selectByExampleWithRowbounds(EmployeeHistoryExample example, RowBounds rowBounds);

    List<EmployeeHistory> selectByExample(EmployeeHistoryExample example);

    EmployeeHistory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") EmployeeHistory record, @Param("example") EmployeeHistoryExample example);

    int updateByExample(@Param("record") EmployeeHistory record, @Param("example") EmployeeHistoryExample example);

    int updateByPrimaryKeySelective(EmployeeHistory record);

    int updateByPrimaryKey(EmployeeHistory record);
}