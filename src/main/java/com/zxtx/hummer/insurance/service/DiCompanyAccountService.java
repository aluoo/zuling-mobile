package com.zxtx.hummer.insurance.service;

import com.zxtx.hummer.insurance.domain.DiCompanyAccount;
import com.zxtx.hummer.insurance.dao.mapper.DiCompanyAccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.insurance.domain.DiCompanyAccountLog;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数保门店账户表 服务实现类
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Service
public class DiCompanyAccountService extends ServiceImpl<DiCompanyAccountMapper, DiCompanyAccount>{

    public boolean changeAccountBalance(DiCompanyAccountLog accountLog) {
        int updates = this.getBaseMapper().changeAccountBalance(accountLog);
        return updates > 0;
    }

}
