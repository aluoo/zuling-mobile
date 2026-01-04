package com.zxtx.hummer.account.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxtx.hummer.account.domain.EmployeeAccount;
import com.zxtx.hummer.account.domain.EmployeeAccountLog;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 个人账户表 Mapper 接口
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface EmployeeAccountMapper extends BaseMapper<EmployeeAccount> {

    int changeAccountBalance(@Param("employeeAccountLog") EmployeeAccountLog employeeAccountLog);
}
