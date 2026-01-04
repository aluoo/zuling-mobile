package com.zxtx.hummer.em.dao.mapper;


import com.zxtx.hummer.em.domain.EmployeeLogin;
import com.zxtx.hummer.em.domain.EmployeeLoginExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;


public interface EmployeeLoginMapper {

    long countByExample(EmployeeLoginExample example);

    int deleteByExample(EmployeeLoginExample example);

    int deleteByPrimaryKey(Long id);

    int insert(EmployeeLogin record);

    int insertSelective(EmployeeLogin record);

    List<EmployeeLogin> selectByExampleWithRowbounds(EmployeeLoginExample example, RowBounds rowBounds);

    List<EmployeeLogin> selectByExample(EmployeeLoginExample example);

    EmployeeLogin selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") EmployeeLogin record, @Param("example") EmployeeLoginExample example);

    int updateByExample(@Param("record") EmployeeLogin record, @Param("example") EmployeeLoginExample example);

    int updateByPrimaryKeySelective(EmployeeLogin record);

    int updateByPrimaryKey(EmployeeLogin record);
}