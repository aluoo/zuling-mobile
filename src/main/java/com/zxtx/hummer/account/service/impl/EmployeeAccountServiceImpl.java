package com.zxtx.hummer.account.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.account.dao.mapper.EmployeeAccountMapper;
import com.zxtx.hummer.account.domain.EmployeeAccount;
import com.zxtx.hummer.account.domain.EmployeeAccountLog;
import com.zxtx.hummer.account.service.IEmployeeAccountService;
import com.zxtx.hummer.exchange.domain.MbExchangeEmployeeInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 个人账户表 服务实现类
 * </p>
 *
 * @author shenbh
 * @since 2023-03-02
 */
@Service
public class EmployeeAccountServiceImpl extends ServiceImpl<EmployeeAccountMapper, EmployeeAccount> implements IEmployeeAccountService {

    @Override
    public boolean changeAccountBalance(EmployeeAccountLog accountLog) {
        int updates = this.getBaseMapper().changeAccountBalance(accountLog);
        return updates > 0;
    }

    @Override
    public EmployeeAccount getByEmployeeId(Long employeeId){
        List<EmployeeAccount> resultList =  this.list(Wrappers.lambdaQuery(EmployeeAccount.class)
                .eq(EmployeeAccount::getEmployeeId,employeeId));
        if(CollUtil.isEmpty(resultList)) return null;
        return resultList.get(0);
    }
}
