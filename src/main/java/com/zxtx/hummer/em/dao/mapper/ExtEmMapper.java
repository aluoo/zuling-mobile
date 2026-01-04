package com.zxtx.hummer.em.dao.mapper;

import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.vo.EmLsReq;
import com.zxtx.hummer.em.vo.EmLsRps;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ExtEmMapper {

    void changeChannelEmStatus(@Param("code") String code, @Param("employee") Employee employee, @Param("oldStatus") Byte oldStatus);


    void updateDeptCode(@Param("cmpIds") List cmpIds, @Param("deptCode") String deptCode, @Param("updateTime") Date updateTime,
                        @Param("updator") String updator, @Param("newCmpId") Long newCmpId, @Param("newCompanyType") Integer newCompanyType);

    List<Map<String, String>> queryTokenByDeptCode(@Param("deptCode") String deptCode, @Param("currDate") Date currDate);


    List<EmLsRps> pageListEm(EmLsReq emLsReq);
}