package com.zxtx.hummer.mobileStat.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.exchange.domain.MbExchangeOrder;
import com.zxtx.hummer.exchange.enums.ExchangeOrderTypeEnum;
import com.zxtx.hummer.exchange.enums.ExchangeStatus;
import com.zxtx.hummer.exchange.service.MbExchangeOrderService;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrder;
import com.zxtx.hummer.insurance.enums.DiOrderStatusEnum;
import com.zxtx.hummer.insurance.service.DiInsuranceOrderService;
import com.zxtx.hummer.mobileStat.dto.IndexStatisticsReq;
import com.zxtx.hummer.mobileStat.dto.IndexStatisticsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/12
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class SubAdminStatisticsService {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private MbExchangeOrderService exchangeOrderService;
    @Autowired
    private DiInsuranceOrderService insuranceOrderService;

    public IndexStatisticsVO indexStatistics(IndexStatisticsReq req) {
        if (req.getStartTime() == null) {
            req.setStartTime(DateUtil.beginOfDay(DateUtil.date()));
        }
        if (req.getEndTime() == null) {
            req.setEndTime(DateUtil.endOfDay(DateUtil.date()));
        }
        IndexStatisticsVO vo = IndexStatisticsVO.builder()
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .build();
        Integer employeeRoleType = ShiroUtils.getUser().getEmployeeRoleType();
        if (employeeRoleType == null) {
            return vo;
        }
        //当前登陆人下级的
        String employeeAncestors = ShiroUtils.getUser().getEmployeeAncestors();
        if (StrUtil.isBlank(employeeAncestors)) {
            return vo;
        }
        Set<Long> employeeIds = employeeMapper.selectList(new LambdaQueryWrapper<Employee>()
                .likeRight(Employee::getAncestors, employeeAncestors)
                // .eq(Employee::getStatus, EmStatus.NORMAL.getCode())
                ).stream().map(Employee::getId)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(employeeIds)) {
            return vo;
        }
        // 总拉新订单
        Long exchangeOrderAllTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .count();
        Long exchangeOrderAllPassTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .eq(MbExchangeOrder::getStatus, ExchangeStatus.PASS.getCode())
                .count();
        vo.setExchangeOrderAllTotal(exchangeOrderAllTotal);
        vo.setExchangeOrderAllPassTotal(exchangeOrderAllPassTotal);
        // 换机
        Long exchangeOrderHJTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .eq(MbExchangeOrder::getType, ExchangeOrderTypeEnum.HUAN_JI.getCode())
                .count();
        Long exchangeOrderHJPassTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .eq(MbExchangeOrder::getType, ExchangeOrderTypeEnum.HUAN_JI.getCode())
                .eq(MbExchangeOrder::getStatus, ExchangeStatus.PASS.getCode())
                .count();
        vo.setExchangeOrderHJTotal(exchangeOrderHJTotal);
        vo.setExchangeOrderHJPassTotal(exchangeOrderHJPassTotal);
        // 快手绿洲
        Long exchangeOrderKSLZTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .eq(MbExchangeOrder::getType, ExchangeOrderTypeEnum.LV_ZHOU.getCode())
                .count();
        Long exchangeOrderKSLZPassTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .eq(MbExchangeOrder::getType, ExchangeOrderTypeEnum.LV_ZHOU.getCode())
                .eq(MbExchangeOrder::getStatus, ExchangeStatus.PASS.getCode())
                .count();
        vo.setExchangeOrderKSLZTotal(exchangeOrderKSLZTotal);
        vo.setExchangeOrderKSLZPassTotal(exchangeOrderKSLZPassTotal);
        // 苹果抖音
        Long exchangeOrderPGDYTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .eq(MbExchangeOrder::getType, ExchangeOrderTypeEnum.IPHONE_DOUYIN.getCode())
                .count();
        Long exchangeOrderPGDYPassTotal = exchangeOrderService.lambdaQuery()
                .between(MbExchangeOrder::getTrialTime, req.getStartTime(), req.getEndTime())
                .in(MbExchangeOrder::getStoreEmployeeId, employeeIds)
                .eq(MbExchangeOrder::getType, ExchangeOrderTypeEnum.IPHONE_DOUYIN.getCode())
                .eq(MbExchangeOrder::getStatus, ExchangeStatus.PASS.getCode())
                .count();
        vo.setExchangeOrderPGDYTotal(exchangeOrderPGDYTotal);
        vo.setExchangeOrderPGDYPassTotal(exchangeOrderPGDYPassTotal);
        // 数保
        Long insuranceOrderAllTotal = insuranceOrderService.lambdaQuery()
                .between(AbstractBaseEntity::getCreateTime, req.getStartTime(), req.getEndTime())
                .in(DiInsuranceOrder::getStoreEmployeeId, employeeIds)
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.FINISHED.getCode())
                .count();
        Long insuranceOrderCareTotal = insuranceOrderService.lambdaQuery()
                .between(AbstractBaseEntity::getCreateTime, req.getStartTime(), req.getEndTime())
                .in(DiInsuranceOrder::getStoreEmployeeId, employeeIds)
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.FINISHED.getCode())
                .eq(DiInsuranceOrder::getInsuranceType, "Care+")
                .count();
        Long insuranceOrderSPBTotal = insuranceOrderService.lambdaQuery()
                .between(AbstractBaseEntity::getCreateTime, req.getStartTime(), req.getEndTime())
                .in(DiInsuranceOrder::getStoreEmployeeId, employeeIds)
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.FINISHED.getCode())
                .eq(DiInsuranceOrder::getInsuranceType, "碎屏保")
                .count();
        Long insuranceOrderYBTotal = insuranceOrderService.lambdaQuery()
                .between(AbstractBaseEntity::getCreateTime, req.getStartTime(), req.getEndTime())
                .in(DiInsuranceOrder::getStoreEmployeeId, employeeIds)
                .eq(DiInsuranceOrder::getStatus, DiOrderStatusEnum.FINISHED.getCode())
                .eq(DiInsuranceOrder::getInsuranceType, "延长保")
                .count();
        vo.setInsuranceOrderAllTotal(insuranceOrderAllTotal);
        vo.setInsuranceOrderCareTotal(insuranceOrderCareTotal);
        vo.setInsuranceOrderSPBTotal(insuranceOrderSPBTotal);
        vo.setInsuranceOrderYBTotal(insuranceOrderYBTotal);
        return vo;
    }
}