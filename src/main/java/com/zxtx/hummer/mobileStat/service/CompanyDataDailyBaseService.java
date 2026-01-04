package com.zxtx.hummer.mobileStat.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.commission.dao.mapper.CommissionSettleDataDailyTotalMapper;
import com.zxtx.hummer.exchange.dao.mapper.MbExchangeOrderMapper;
import com.zxtx.hummer.insurance.dao.mapper.DiInsuranceOrderMapper;
import com.zxtx.hummer.mobileStat.dao.mapper.CompanyDataDailyBaseMapper;
import com.zxtx.hummer.mobileStat.domain.CompanyDataDailyBase;
import com.zxtx.hummer.order.dao.mapper.MbOrderMapper;
import com.zxtx.hummer.order.dao.mapper.MbOrderQuotePriceLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 门店统计日看板表 服务实现类
 * </p>
 *
 * @author L
 * @since 2024-03-08
 */
@Service
@Slf4j
public class CompanyDataDailyBaseService extends ServiceImpl<CompanyDataDailyBaseMapper, CompanyDataDailyBase> {

    @Autowired
    private MbOrderMapper mbOrderMapper;
    @Autowired
    private MbOrderQuotePriceLogMapper mbOrderQuotePriceLogMapper;
    @Autowired
    CommissionSettleDataDailyTotalMapper commissionSettleDataDailyTotalMapper;
    @Autowired
    private MbExchangeOrderMapper exchangeOrderMapper;
    @Autowired
    private DiInsuranceOrderMapper insuranceOrderMapper;

    @Transactional(rollbackFor = Exception.class)
    public void pullAllByDay(String dateStr, boolean removeExist) {
        log.info("二手机门店每日统计开始");
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
            log.info("二手机门店统计存在日期", beginOfTargetDay);
            if (removeExist) {
                log.info("二手机门店统计存在日期数据清除", beginOfTargetDay);
                removeExist(beginOfTargetDay.toJdkDate());
            }
        }
        //最终提交的每个用户统计数据
        Map<Long, CompanyDataDailyBase> employeeStat = new HashMap<>();
        //交易数量
        List<CompanyDataDailyBase> transList = mbOrderMapper.companyTransStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(transList)) {
            for (CompanyDataDailyBase dataDailyBase : transList) {
                employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
            }
        }
        //询价数量
        List<CompanyDataDailyBase> ordertList = mbOrderMapper.companyOrderStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(ordertList)) {
            for (CompanyDataDailyBase dataDailyBase : ordertList) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setOrderNum(dataDailyBase.getOrderNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }
        //报价数量 成交金额
        List<CompanyDataDailyBase> priceList = mbOrderMapper.priceOrderStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(priceList)) {
            for (CompanyDataDailyBase dataDailyBase : priceList) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setPriceNum(dataDailyBase.getPriceNum());
                    old.setFinalPriceAmount(dataDailyBase.getFinalPriceAmount());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //收益金额
        List<CompanyDataDailyBase> commissionList = commissionSettleDataDailyTotalMapper.commissionStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(commissionList)) {
            for (CompanyDataDailyBase dataDailyBase : commissionList) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setCommissionAmount(dataDailyBase.getCommissionAmount());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }
        //取消次数
        List<CompanyDataDailyBase> cancelList = mbOrderMapper.cancelStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(cancelList)) {
            for (CompanyDataDailyBase dataDailyBase : cancelList) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setCancelNum(dataDailyBase.getCancelNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }
        //作废次数
        List<CompanyDataDailyBase> overTimeList = mbOrderMapper.overTimeStatGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(overTimeList)) {
            for (CompanyDataDailyBase dataDailyBase : overTimeList) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setOvertimeNum(dataDailyBase.getOvertimeNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }
        //拉新订单数
        List<CompanyDataDailyBase>  exchangeAll = exchangeOrderMapper.statAllGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(exchangeAll)) {
            for (CompanyDataDailyBase dataDailyBase : exchangeAll) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setExchangeNum(dataDailyBase.getExchangeNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //拉新通过订单数
        List<CompanyDataDailyBase>  exchangePass = exchangeOrderMapper.statPassGroupByEmployee(beginOfTargetDay, endOfTargetDay);
        if (CollUtil.isNotEmpty(exchangePass)) {
            for (CompanyDataDailyBase dataDailyBase : exchangePass) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setExchangePassNum(dataDailyBase.getExchangePassNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //数保全保订单
        List<CompanyDataDailyBase> insuranceOne = insuranceOrderMapper.statGroupByEmployee(beginOfTargetDay, endOfTargetDay,"全保");
        if (CollUtil.isNotEmpty(insuranceOne)) {
            for (CompanyDataDailyBase dataDailyBase : insuranceOne) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setInsuranceAnyNum(dataDailyBase.getInsuranceAnyNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //数保碎屏投保
        List<CompanyDataDailyBase> insuranceTwo = insuranceOrderMapper.statGroupByEmployee(beginOfTargetDay, endOfTargetDay,"碎屏保");
        if (CollUtil.isNotEmpty(insuranceTwo)) {
            for (CompanyDataDailyBase dataDailyBase : insuranceTwo) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setInsuranceSpNum(dataDailyBase.getInsuranceAnyNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //数保延长保数目
        List<CompanyDataDailyBase> insuranceThree = insuranceOrderMapper.statGroupByEmployee(beginOfTargetDay, endOfTargetDay,"延长保");
        if (CollUtil.isNotEmpty(insuranceThree)) {
            for (CompanyDataDailyBase dataDailyBase : insuranceThree) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setInsuranceYbNum(dataDailyBase.getInsuranceAnyNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //数保CARE投保数目
        List<CompanyDataDailyBase> insuranceFour = insuranceOrderMapper.statGroupByEmployee(beginOfTargetDay, endOfTargetDay,"Care+");
        if (CollUtil.isNotEmpty(insuranceFour)) {
            for (CompanyDataDailyBase dataDailyBase : insuranceFour) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setInsuranceCareNum(dataDailyBase.getInsuranceAnyNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        //数保安卓终身数目
        List<CompanyDataDailyBase> insuranceFive = insuranceOrderMapper.statGroupByEmployee(beginOfTargetDay, endOfTargetDay,"安卓终身保");
        if (CollUtil.isNotEmpty(insuranceFive)) {
            for (CompanyDataDailyBase dataDailyBase : insuranceFive) {
                CompanyDataDailyBase old = employeeStat.get(dataDailyBase.getEmployeeId());
                if (old != null) {
                    old.setInsuranceAzNum(dataDailyBase.getInsuranceAnyNum());
                } else {
                    employeeStat.put(dataDailyBase.getEmployeeId(), dataDailyBase);
                }
            }
        }

        List<CompanyDataDailyBase> saveList = new ArrayList<>();
        employeeStat.forEach((key, value) -> {
            value.setDay(beginOfTargetDay.toJdkDate());
            saveList.add(value);
        });

        this.saveBatch(saveList);
    }

    private boolean exist(Date day) {
        return this.lambdaQuery()
                .eq(CompanyDataDailyBase::getDay, day)
                .exists();
    }

    private void removeExist(Date day) {
        this.remove(new LambdaQueryWrapper<CompanyDataDailyBase>()
                .eq(CompanyDataDailyBase::getDay, day));
    }




}
