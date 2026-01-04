package com.zxtx.hummer.account.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.account.domain.EmployeeAccountLog;
import com.zxtx.hummer.account.dto.EmployeeAccountLogDTO;

import java.util.List;

/**
 * <p>
 * 个人账户变动明细表 服务类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
public interface IEmployeeAccountLogService extends IService<EmployeeAccountLog> {

    List<EmployeeAccountLogDTO> pageList(Long employeeId, int page, int pageSize);
}
