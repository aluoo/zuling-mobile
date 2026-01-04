package com.zxtx.hummer.mobileStat.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyBase;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyDetail;
import com.zxtx.hummer.commission.domain.CommissionSettleDataDailyTotal;
import com.zxtx.hummer.mobileStat.domain.RecycleDataDailyBase;
import com.zxtx.hummer.mobileStat.dao.mapper.RecycleDataDailyBaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.order.dao.mapper.MbOrderMapper;
import com.zxtx.hummer.order.dao.mapper.MbOrderQuotePriceLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 回收商统计日看板表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Service
@Slf4j
public class RecycleDataDailyBaseService extends ServiceImpl<RecycleDataDailyBaseMapper, RecycleDataDailyBase> {

    @Autowired
    private MbOrderMapper mbOrderMapper;
    @Autowired
    private MbOrderQuotePriceLogMapper mbOrderQuotePriceLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void pullAllByDay(String dateStr, boolean removeExist) {
        log.info("二手机服务商每日统计开始");
        // 每日拉取前一日数据进行统计
        DateTime now = DateUtil.date();
        DateTime targetDay;
        if (StrUtil.isNotBlank(dateStr)) {
            targetDay = DateUtil.parse(dateStr);
        } else {
            targetDay = DateUtil.yesterday();
        }
        DateTime beginOfTargetDay = DateUtil.beginOfDay(targetDay);
        DateTime endOfTargetDay = DateUtil.endOfDay(targetDay);

        if (exist(beginOfTargetDay.toJdkDate())) {
            log.info("二手机服务商统计存在日期", beginOfTargetDay);
            if (removeExist) {
                log.info("二手机服务商统计存在日期数据清除", beginOfTargetDay);
                removeExist(beginOfTargetDay.toJdkDate());
            }
        }
        //最终提交的每个用户统计数据
        Map<Long, RecycleDataDailyBase> employeeStat = new HashMap<>();
        //交易数量，交易金额统计
        List<RecycleDataDailyBase> transList = mbOrderMapper.tranStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(transList)) {
            for (RecycleDataDailyBase dataDailyBase : transList) {
                employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
            }
        }
        //确认收货量
        List<RecycleDataDailyBase> receiptList = mbOrderMapper.receiptStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(transList)) {
            for (RecycleDataDailyBase dataDailyBase : receiptList) {
                RecycleDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setOrderConfirmNum(dataDailyBase.getOrderConfirmNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }
        //退款金额
        List<RecycleDataDailyBase> refundList = mbOrderMapper.refundGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(refundList)) {
            for (RecycleDataDailyBase dataDailyBase : refundList) {
                RecycleDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setRefundAmount(dataDailyBase.getRefundAmount());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //出价次数，出价时长
        List<RecycleDataDailyBase> quoteList = mbOrderQuotePriceLogMapper.quoteCountGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(quoteList)) {
            for (RecycleDataDailyBase dataDailyBase : quoteList) {
                RecycleDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setQuotePriceNum(dataDailyBase.getQuotePriceNum());
                    old.setQuoteTimeSpent(dataDailyBase.getQuoteTimeSpent());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        List<RecycleDataDailyBase> saveList = new ArrayList<>();
        employeeStat.forEach((key, value) -> {
            value.setDay(beginOfTargetDay.toJdkDate());
            saveList.add(value);
        });

        this.saveBatch(saveList);
    }

    private boolean exist(Date day) {
        return this.lambdaQuery()
                .eq(RecycleDataDailyBase::getDay, day)
                .exists();
    }

    private void removeExist(Date day) {
        this.remove(new LambdaQueryWrapper<RecycleDataDailyBase>()
                .eq(RecycleDataDailyBase::getDay, day));
    }

}
