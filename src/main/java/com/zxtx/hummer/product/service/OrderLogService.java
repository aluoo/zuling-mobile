package com.zxtx.hummer.product.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.SnowflakeIdWorker;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.insurance.domain.DiInsuranceUserAccount;
import com.zxtx.hummer.insurance.service.DiInsuranceUserAccountService;
import com.zxtx.hummer.product.dao.OrderLogMapper;
import com.zxtx.hummer.product.domain.OrderLog;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.system.dao.mapper.SysUserMapper;
import com.zxtx.hummer.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/3/18
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderLogService extends ServiceImpl<OrderLogMapper, OrderLog> {
    public static final Long UNKNOWN_OPERATOR = -1L;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    EmService emService;
    @Autowired
    DiInsuranceUserAccountService insuranceUserAccountService;

    public PageUtils<OrderLogDTO> listLogsByOrderId(OrderLogQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<OrderLog> list = this.listLogs(req.getOrderId());
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<OrderLogDTO> vos = buildLogs(list);
        return new PageUtils<>(vos, page.getTotal());
    }

    public List<OrderLogDTO> listAllLogsByOrderId(OrderLogQueryReq req) {
        if (req.getOrderId() == null) {
            return new ArrayList<>();
        }
        List<OrderLog> list = this.listLogs(req.getOrderId());
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return buildLogs(list);
    }

    private List<OrderLog> listLogs(Long orderId) {
        if (orderId == null) {
            return new ArrayList<>();
        }
        return this.list(Wrappers.lambdaQuery(OrderLog.class)
                .eq(OrderLog::getOrderId, orderId)
                .eq(OrderLog::getDeleted, false)
                .orderByDesc(OrderLog::getCreateTime));
    }

    public List<OrderLogDTO> buildLogs(List<OrderLog> sources) {
        if (CollUtil.isEmpty(sources)) {
            return new ArrayList<>();
        }
        List<OrderLogDTO> list = BeanUtil.copyToList(sources, OrderLogDTO.class);
        Set<Long> userIds = list.stream().map(OrderLogDTO::getCreateBy).collect(Collectors.toSet());

        Map<Long, SysUser> sysUserMap = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().in(SysUser::getUserId, userIds)).stream().filter(Objects::nonNull).collect(Collectors.toMap(SysUser::getUserId, Function.identity()));
        Map<Long, Employee> empMap = emService.getEmployeeByMap(userIds);
        Map<Long, DiInsuranceUserAccount> userMap = insuranceUserAccountService.getUserMap(userIds);

        list.forEach(e -> {
            // e.setStatusStr(ExchangeStatus.getNameByCode(e.getStatus()));
            // e.setOperationStatusStr(ExchangeOrderLogStatus.getNameByCode(e.getOperationStatus()));
            e.setCreator(Optional.ofNullable(sysUserMap.get(e.getCreateBy())).map(SysUser::getName).orElse(Optional.ofNullable(empMap.get(e.getCreateBy())).map(Employee::getName).orElse(Optional.ofNullable(userMap.get(e.getCreateBy())).map(DiInsuranceUserAccount::getName).orElse(null))));
        });
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void addLog(Long operator, Long orderId, int status, int operationStatus, String operation, String remark) {
        if (orderId == null) {
            return;
        }
        if (operator == null) {
            operator = UNKNOWN_OPERATOR;
        }
        OrderLog log = OrderLog.builder()
                .id(SnowflakeIdWorker.nextID())
                .createBy(operator)
                .createTime(new Date())
                .deleted(false)
                .orderId(orderId)
                .status(status)
                .operationStatus(operationStatus)
                .operation(operation)
                .remark(remark)
                .build();
        this.save(log);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addLogBatch(Long operator, List<Long> orderIds, int status, int operationStatus, String operation, String remark) {
        if (CollUtil.isEmpty(orderIds)) {
            return;
        }
        if (operator == null) {
            operator = UNKNOWN_OPERATOR;
        }
        List<OrderLog> list = new ArrayList<>();
        for (Long orderId : orderIds) {
            list.add(OrderLog.builder()
                    .id(SnowflakeIdWorker.nextID())
                    .createBy(operator)
                    .createTime(new Date())
                    .deleted(false)
                    .orderId(orderId)
                    .status(status)
                    .operationStatus(operationStatus)
                    .operation(operation)
                    .remark(remark)
                    .build());
        }
        if (CollUtil.isNotEmpty(list)) {
            this.saveBatch(list);
        }
    }
}