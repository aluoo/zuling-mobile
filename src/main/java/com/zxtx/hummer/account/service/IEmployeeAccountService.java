package com.zxtx.hummer.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.account.domain.EmployeeAccount;
import com.zxtx.hummer.account.domain.EmployeeAccountLog;

/**
 * <p>
 * 个人账户表 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface IEmployeeAccountService extends IService<EmployeeAccount> {

    boolean changeAccountBalance(EmployeeAccountLog accountLog);

    EmployeeAccount getByEmployeeId(Long employeeId);
}
