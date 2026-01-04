package com.zxtx.hummer.hk.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.account.constant.EmployAccountChangeEnum;
import com.zxtx.hummer.account.service.EmployeeAccountChangeService;
import com.zxtx.hummer.commission.domain.CommissionSettle;
import com.zxtx.hummer.commission.domain.CommissionSettleCheck;
import com.zxtx.hummer.commission.service.CommissionSettleCheckService;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.exception.BusinessException;
import com.zxtx.hummer.common.lock.RedisLockService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.utils.StringUtils;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.exchange.domain.MbExchangeOrder;
import com.zxtx.hummer.exchange.domain.dto.ExchangeOrderCommissionBackDTO;
import com.zxtx.hummer.exchange.enums.ExchangeOrderLogStatus;
import com.zxtx.hummer.exchange.enums.ExchangeSettleStatus;
import com.zxtx.hummer.exchange.enums.ExchangeStatus;
import com.zxtx.hummer.hk.domain.HkApplyOrder;
import com.zxtx.hummer.hk.domain.HkOperator;
import com.zxtx.hummer.hk.domain.HkSupplier;
import com.zxtx.hummer.hk.domain.enums.HkApplyOrderStatusEnum;
import com.zxtx.hummer.hk.domain.request.HkApplyOrderQueryReq;
import com.zxtx.hummer.hk.domain.response.HKApplyOrderVO;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.product.service.OrderLogService;
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
 * @Date 2025/8/4
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Service
public class HkApplyOrderService {
    @Autowired
    private HkApplyOrderRepository hkApplyOrderRepository;
    @Autowired
    private HkSupplierRepository hkSupplierRepository;
    @Autowired
    private HkOperatorRepository hkOperatorRepository;
    @Autowired
    private OrderLogService orderLogService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RedisLockService redisLockService;
    @Autowired
    private CommissionSettleService commissionSettleService;
    @Autowired
    private EmployeeAccountChangeService employeeAccountChangeService;
    @Autowired
    private CommissionSettleCheckService commissionSettleCheckService;

    public PageUtils<HKApplyOrderVO> list(HkApplyOrderQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        Collection<Long> searchEmployeeId = new ArrayList<>();
        Collection<Long> searchCompanyId = new ArrayList<>();
        if (StrUtil.isNotBlank(req.getEmployeeMobile())) {
            List<Employee> employees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().eq(Employee::getMobileNumber, req.getEmployeeMobile()));
            if (CollUtil.isNotEmpty(employees)) {
                Set<Long> collect = employees.stream().map(Employee::getId).collect(Collectors.toSet());
                searchEmployeeId.addAll(collect);
            }
        }
        if (StrUtil.isNotBlank(req.getEmployeeName())) {
            List<Employee> employees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().like(Employee::getName, req.getEmployeeName()));
            if (CollUtil.isNotEmpty(employees)) {
                Set<Long> collect = employees.stream().map(Employee::getId).collect(Collectors.toSet());
                searchEmployeeId.addAll(collect);
            }
        }
        if (StrUtil.isNotBlank(req.getCompanyName())) {
            List<Company> companies = companyService.lambdaQuery().like(Company::getName, req.getCompanyName()).list();
            if (CollUtil.isNotEmpty(companies)) {
                Set<Long> collect = companies.stream().map(Company::getId).collect(Collectors.toSet());
                searchCompanyId.addAll(collect);
            }
        }
        List<HkApplyOrder> list = hkApplyOrderRepository.lambdaQuery()
                .in(CollUtil.isNotEmpty(searchEmployeeId), HkApplyOrder::getEmployeeId, searchEmployeeId)
                .in(CollUtil.isNotEmpty(searchCompanyId), HkApplyOrder::getCompanyId, searchCompanyId)
                .eq(req.getId() != null, HkApplyOrder::getId, req.getId())
                .eq(StrUtil.isNotBlank(req.getFetchCode()), HkApplyOrder::getFetchCode, req.getFetchCode())
                .eq(StrUtil.isNotBlank(req.getPlanMobileNumber()), HkApplyOrder::getPlanMobileNumber, req.getPlanMobileNumber())
                .eq(StrUtil.isNotBlank(req.getName()), HkApplyOrder::getName, req.getName())
                .eq(StrUtil.isNotBlank(req.getProvinceName()), HkApplyOrder::getProvinceName, req.getProvinceName())
                .eq(StrUtil.isNotBlank(req.getCityName()), HkApplyOrder::getCityName, req.getCityName())
                .eq(StrUtil.isNotBlank(req.getTownName()), HkApplyOrder::getTownName, req.getTownName())
                .eq(req.getStatus() != null, HkApplyOrder::getStatus, req.getStatus())
                .eq(req.getSupplierId() != null, HkApplyOrder::getSupplierId, req.getSupplierId())
                .eq(req.getOperatorId() != null, HkApplyOrder::getOperatorId, req.getOperatorId())
                .gt(req.getCreateStartTime() != null, HkApplyOrder::getCreateTime, req.getCreateStartTime())
                .lt(req.getCreateEndTime() != null, HkApplyOrder::getCreateTime, req.getCreateEndTime())
                .gt(req.getActiveStartTime() != null, HkApplyOrder::getActiveTime, req.getActiveStartTime())
                .lt(req.getActiveEndTime() != null, HkApplyOrder::getActiveTime, req.getActiveEndTime())
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByDesc(HkApplyOrder::getCreateTime)
                .orderByDesc(HkApplyOrder::getActiveTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<HKApplyOrderVO> resp = BeanUtil.copyToList(list, HKApplyOrderVO.class);

        Set<Long> companyIds = resp.stream().map(HKApplyOrderVO::getCompanyId).collect(Collectors.toSet());
        Set<Long> employeeIds = resp.stream().map(HKApplyOrderVO::getEmployeeId).collect(Collectors.toSet());
        List<Company> companyList = companyService.lambdaQuery().in(Company::getId, companyIds).list();
        Map<Long, Company> companyMap = companyList.stream().collect(Collectors.toMap(Company::getId, Function.identity()));
        List<Employee> employees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, employeeIds));
        Map<Long, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));
        Set<Long> supplierIds = resp.stream().map(HKApplyOrderVO::getSupplierId).collect(Collectors.toSet());
        Set<Long> operatorIds = resp.stream().map(HKApplyOrderVO::getOperatorId).collect(Collectors.toSet());
        Map<Long, HkOperator> operatorMap = hkOperatorRepository.lambdaQuery().in(HkOperator::getId, operatorIds).list().stream().collect(Collectors.toMap(HkOperator::getId, Function.identity()));
        Map<Long, HkSupplier> supplierMap = hkSupplierRepository.lambdaQuery().in(HkSupplier::getId, supplierIds).list().stream().collect(Collectors.toMap(HkSupplier::getId, Function.identity()));

        resp.forEach(o -> {
            o.setStatusStr(Optional.ofNullable(EnumUtil.getBy(HkApplyOrderStatusEnum::getCode, o.getStatus()))
                    .map(HkApplyOrderStatusEnum::getDesc)
                    .orElse(HkApplyOrderStatusEnum.UNKNOWN.getDesc()));
            o.setEmployeeName(Optional.ofNullable(employeeMap.get(o.getEmployeeId())).map(Employee::getName).orElse(null));
            o.setEmployeeMobile(Optional.ofNullable(employeeMap.get(o.getEmployeeId())).map(Employee::getMobileNumber).orElse(null));
            o.setCompanyName(Optional.ofNullable(companyMap.get(o.getCompanyId())).map(Company::getName).orElse(null));
            o.setArea(StringUtils.buildArea(o.getProvinceName(), o.getCityName(), o.getTownName(), "/"));
            o.setOperator(Optional.ofNullable(operatorMap.get(o.getOperatorId())).map(HkOperator::getName).orElse(null));
            o.setSupplier(Optional.ofNullable(supplierMap.get(o.getSupplierId())).map(HkSupplier::getName).orElse(null));
        });

        return new PageUtils<>(resp, page.getTotal());
    }

    public HKApplyOrderVO detail(Long id) {
        HkApplyOrder bean = Optional.ofNullable(hkApplyOrderRepository.lambdaQuery()
                        .eq(HkApplyOrder::getId, id)
                        .eq(AbstractBaseEntity::getDeleted, false)
                .one()).orElseThrow(() -> new BaseException(-1, "数据不存在"));
        HKApplyOrderVO resp = BeanUtil.copyProperties(bean, HKApplyOrderVO.class);

        Company company = companyService.getById(resp.getCompanyId());
        Employee employee = employeeMapper.selectById(resp.getEmployeeId());
        Company chainCompany = null;
        if (company != null && company.getPId() != null && !company.getPId().equals(Constants.LAN_HAI_CMP_ID)) {
            chainCompany = companyService.getById(company.getPId());
        }
        resp.setCompanyName(Optional.ofNullable(company).map(Company::getName).orElse(null));
        resp.setEmployeeName(Optional.ofNullable(employee).map(Employee::getName).orElse(null));
        resp.setEmployeeMobile(Optional.ofNullable(employee).map(Employee::getMobileNumber).orElse(null));

        HkOperator operator = hkOperatorRepository.getById(resp.getOperatorId());
        HkSupplier supplier = hkSupplierRepository.getById(resp.getSupplierId());

        resp.setSupplier(Optional.ofNullable(supplier).map(HkSupplier::getName).orElse(null));
        resp.setOperator(Optional.ofNullable(operator).map(HkOperator::getName).orElse(null));
        resp.setChainStoreName(Optional.ofNullable(chainCompany).map(Company::getName).orElse(null));

        List<OrderLogDTO> logs = orderLogService.listAllLogsByOrderId(OrderLogQueryReq.builder().orderId(id).build());
        if (CollUtil.isNotEmpty(logs)) {
            logs.forEach(o-> {
                o.setStatusStr(EnumUtil.getBy(HkApplyOrderStatusEnum::getCode, o.getStatus()).getDesc());
                o.setOperationStatusStr(EnumUtil.getBy(HkApplyOrderStatusEnum::getCode, o.getOperationStatus()).getDesc());
            });
        }
        resp.setLogs(logs);
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    public void commissionBack(ExchangeOrderCommissionBackDTO req) {
        Long orderId = req.getId();
        redisLockService.redisLock("hk_order_commission_back_lock", orderId);
        HkApplyOrder applyOrder = Optional.ofNullable(hkApplyOrderRepository.getById(orderId)).orElseThrow(() -> new BusinessException("订单不存在"));

        List<CommissionSettle> resultList = commissionSettleService.lambdaQuery()
                .eq(CommissionSettle::getCorrelationId,orderId).list();
        if (CollUtil.isNotEmpty(resultList)) {
            for (CommissionSettle settle : resultList) {
                String remark = "系统佣金追回";
                if (StrUtil.isNotBlank(req.getRemark())) {
                    remark = StrUtil.format("{}: {}", remark, req.getRemark());
                }
                employeeAccountChangeService.changeAccount(settle.getEmployeeId(), EmployAccountChangeEnum.sys_commission_back, settle.getSettleBalance(), settle.getCorrelationId(), remark);
            }
            //删除结算
            commissionSettleService.remove(Wrappers.lambdaQuery(CommissionSettle.class).eq(CommissionSettle::getCorrelationId,orderId));
            //删除合伙人账单
            commissionSettleCheckService.remove(Wrappers.lambdaQuery(CommissionSettleCheck.class).eq(CommissionSettleCheck::getCorrelationId,orderId));
        }
        String remark = "超管佣金追回";
        if (StrUtil.isNotBlank(req.getRemark())) {
            remark = StrUtil.format("{}: {}", remark, req.getRemark());
        }
        orderLogService.addLog(
                ShiroUtils.getUserId() != null ? ShiroUtils.getUserId() : 2L,
                orderId,
                ExchangeStatus.FAIL.getCode(), ExchangeOrderLogStatus.FAIL.getCode(),
                ExchangeOrderLogStatus.FAIL.getName(),
                remark);
    }
}