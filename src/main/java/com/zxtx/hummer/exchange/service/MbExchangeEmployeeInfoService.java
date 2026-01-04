package com.zxtx.hummer.exchange.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxtx.hummer.exchange.domain.MbExchangeEmployeeInfo;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeEmployeeInfoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 二手机代理拓展信息表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Service
public class MbExchangeEmployeeInfoService extends ServiceImpl<MbExchangeEmployeeInfoMapper, MbExchangeEmployeeInfo> {

    public MbExchangeEmployeeInfo getByEmployeeId(Long employeeId){
        List<MbExchangeEmployeeInfo> resultList =  this.list(Wrappers.lambdaQuery(MbExchangeEmployeeInfo.class)
                .eq(MbExchangeEmployeeInfo::getEmployeeId,employeeId));
        if(CollUtil.isEmpty(resultList)) return null;
        return resultList.get(0);
    }

}
