package com.zxtx.hummer.em.dao.mapper;

import com.zxtx.hummer.em.domain.EmployeeLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtEmployeeLoginMapper {
//    List<EmployeeLogin> selectByCmpId(@Param("cmpId") Long cmpId);
//
//    int deleteByCmpId(@Param("cmpId") Long cmpId);

    List<EmployeeLogin> selectByDeptCode(@Param("deptCode") String deptCode);

    int deleteByDeptCode(@Param("deptCode") String deptCode);

}