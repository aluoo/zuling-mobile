package com.zxtx.hummer.exchange.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeVerifyEmployeeMapper;
import com.zxtx.hummer.exchange.domain.MbExchangeVerifyEmployee;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 合伙人换机包 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbExchangeVerifyEmployeeService extends ServiceImpl<MbExchangeVerifyEmployeeMapper, MbExchangeVerifyEmployee>{

    public MbExchangeVerifyEmployee getByEmployeeId(Long employeeId,String typeCode){
        return this.lambdaQuery()
                .eq(MbExchangeVerifyEmployee::getVerifyTypeCode,typeCode)
                .eq(MbExchangeVerifyEmployee::getEmployeeId,employeeId).one();
    }

    public int getByStallId(Long stallId,String typeCode){
        return this.lambdaQuery()
                .eq(MbExchangeVerifyEmployee::getVerifyTypeCode,typeCode)
                .eq(MbExchangeVerifyEmployee::getExchangeVerifyId,stallId).list().size();
    }

    public List<MbExchangeVerifyEmployee> getByInstallId(Long installId,String typeCode){
        return this.lambdaQuery()
                .eq(MbExchangeVerifyEmployee::getVerifyTypeCode,typeCode)
                .eq(MbExchangeVerifyEmployee::getExchangeVerifyId,installId).list();
    }

}
