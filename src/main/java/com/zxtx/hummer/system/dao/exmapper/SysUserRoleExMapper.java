package com.zxtx.hummer.system.dao.exmapper;


import com.zxtx.hummer.system.domain.SysUserRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleExMapper {

    int delByUserId(Long userId);

    int batchInsert(List<SysUserRoleDO> list);

    List<SysUserRoleDO> selectByEmployeeId(Long employeeId);

    SysUserRoleDO selectByEmIdAndRoleId(@Param("employeeId") Long employeeId, @Param("roleId") Long roleId);
}
