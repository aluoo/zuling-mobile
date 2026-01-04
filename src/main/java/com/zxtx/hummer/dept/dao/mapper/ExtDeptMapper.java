package com.zxtx.hummer.dept.dao.mapper;

import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.vo.DeptListRs;
import com.zxtx.hummer.dept.vo.DeptListTotalRs;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ExtDeptMapper {

    Integer selectMaxCode(Long pdeptId);

    void changeChannelDeStatus(@Param("code") String code, @Param("dept") Dept dept, @Param("oldStatus") int oldStatus);


    List<DeptListRs> getAllChildDepts(@Param("companyId") Long companyId, @Param("deptCode") String deptCode);

    List<DeptListRs> getAllStatusChildDepts(@Param("companyId") Long companyId, @Param("deptCode") String deptCode);

    List<DeptListRs> getChildDepts(@Param("companyId") Long companyId, @Param("deptId") Long deptId);

    List<DeptListTotalRs> getChildDeptsAndTotal(@Param("companyId") Long companyId, @Param("deptId") Long deptId);


    int queryTotalEm(@Param(value = "deptCode") String deptCode, @Param(value = "childDeptCode") String childDeptCode);

    void deleteChannel(@Param("code") String code, @Param("dept") Dept dept);

    void updateChildsCode(@Param("newPcode") String newPcode, @Param("oldPcode") String oldPcode, @Param("cmpIds") List<Long> cmpIds,
                          @Param("updateTime") Date updateTime, @Param("updator") String updator, @Param("newCmpId") Long newCmpId
            , @Param("newCompanyType") Integer newCompanyType);

    List<Long> queryCmpIdByCode(@Param("code") String code);

    int queryTotalEmAll(@Param("deptId") Long deptId, @Param("code") String code, @Param("companyType") Integer companyType);


}