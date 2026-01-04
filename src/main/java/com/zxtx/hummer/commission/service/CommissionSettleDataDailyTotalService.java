package com.zxtx.hummer.commission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.commission.dao.mapper.CommissionSettleDataDailyTotalMapper;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyTotal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Slf4j
@Service
public class CommissionSettleDataDailyTotalService extends ServiceImpl<CommissionSettleDataDailyTotalMapper, CommissionSettleDataDailyTotal> implements IService<CommissionSettleDataDailyTotal> {
}