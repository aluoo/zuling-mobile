package com.zxtx.hummer.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.dao.mapper.EmployeeAccountLogMapper;
import com.zxtx.hummer.account.domain.EmployeeAccountLog;
import com.zxtx.hummer.account.dto.EmployeeAccountLogDTO;
import com.zxtx.hummer.account.service.IEmployeeAccountLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 个人账户变动明细表 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Service
public class EmployeeAccountLogServiceImpl extends ServiceImpl<EmployeeAccountLogMapper, EmployeeAccountLog> implements IEmployeeAccountLogService {

    @Override
    public List<EmployeeAccountLogDTO> pageList(Long employeeId, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return this.getBaseMapper().selectByEmployeeId(employeeId);
    }
}
