package com.zxtx.hummer.commission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.commission.dao.mapper.CommissionSettleDataDailyDetailMapper;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/13
 */
@Slf4j
@Service
public class CommissionSettleDataDailyDetailService extends ServiceImpl<CommissionSettleDataDailyDetailMapper, CommissionSettleDataDailyDetail> implements IService<CommissionSettleDataDailyDetail> {
}