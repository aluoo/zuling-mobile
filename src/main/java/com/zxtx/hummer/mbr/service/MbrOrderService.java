package com.zxtx.hummer.mbr.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.mbr.dao.MbrOrderMapper;
import com.zxtx.hummer.mbr.domain.MbrOrder;
import com.zxtx.hummer.mbr.domain.dto.HwzOrderDetailDTO;
import com.zxtx.hummer.mbr.domain.enums.MbrOrderStatusEnum;
import com.zxtx.hummer.mbr.req.MbrOrderMarkReq;
import com.zxtx.hummer.mbr.req.MbrOrderReq;
import com.zxtx.hummer.mbr.response.MbrOrderVO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.product.service.OrderLogService;
import com.zxtx.hummer.system.dao.mapper.SysUserMapper;
import com.zxtx.hummer.third.huiwanzu.HuiWanZuApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Slf4j
@Service
public class MbrOrderService extends ServiceImpl<MbrOrderMapper, MbrOrder> {

    @Autowired
    MbrPreOrderQuoteLogService preOrderQuoteLogService;
    @Autowired
    CompanyService companyService;
    @Autowired
    OrderLogService orderLogService;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    public PageUtils<MbrOrderVO> listOrder(MbrOrderReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        Collection<Long> queryStoreCompanyIds = null;
        if (StrUtil.isNotBlank(req.getStoreCompanyName())) {
            List<Company> companies = companyService.lambdaQuery()
                    .like(Company::getName, req.getStoreCompanyName())
                    .list();
            if (CollUtil.isEmpty(companies)) {
                return PageUtils.emptyPage();
            }
            queryStoreCompanyIds = companies.stream().map(Company::getId).collect(Collectors.toSet());
        }
        List<MbrOrder> list = this.lambdaQuery()
                .gt(req.getStartTime() != null, MbrOrder::getCreateTime, req.getStartTime())
                .lt(req.getEndTime() != null, MbrOrder::getCreateTime, req.getEndTime())
                .eq(StrUtil.isNotBlank(req.getCustomName()), MbrOrder::getCustomName, req.getCustomName())
                .eq(ObjectUtil.isNotNull(req.getStatus()), MbrOrder::getStatus, req.getStatus())
                .eq(req.getThirdOrderId() != null, MbrOrder::getThirdOrderId, req.getThirdOrderId())
                .eq(req.getId() != null, MbrOrder::getId, req.getId())
                .in(CollUtil.isNotEmpty(queryStoreCompanyIds), MbrOrder::getStoreCompanyId, queryStoreCompanyIds)
                .eq(AbstractBaseEntity::getDeleted, false)
                .orderByDesc(AbstractBaseEntity::getCreateTime)
                .list();

        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }

        List<MbrOrderVO> resp = BeanUtil.copyToList(list, MbrOrderVO.class);
        Set<Long> companyIds = resp.stream().map(MbrOrderVO::getStoreCompanyId).collect(Collectors.toSet());
        Set<Long> employeeIds = resp.stream().map(MbrOrderVO::getStoreEmployeeId).collect(Collectors.toSet());
        List<Company> companyList = companyService.lambdaQuery().in(Company::getId, companyIds).list();
        Map<Long, Company> companyMap = companyList.stream().collect(Collectors.toMap(Company::getId, Function.identity()));
        List<Employee> employees = employeeMapper.selectList(new LambdaQueryWrapper<Employee>().in(Employee::getId, employeeIds));
        Map<Long, Employee> employeeMap = employees.stream().collect(Collectors.toMap(Employee::getId, Function.identity()));

        resp.forEach(vo -> {
            vo.setStoreCompanyName(Optional.ofNullable(companyMap.get(vo.getStoreCompanyId())).map(Company::getName).orElse(null));
            vo.setEmployeeName(Optional.ofNullable(employeeMap.get(vo.getStoreEmployeeId())).map(Employee::getName).orElse(null));
            vo.setMobileNumber(Optional.ofNullable(employeeMap.get(vo.getStoreEmployeeId())).map(Employee::getMobileNumber).orElse(null));
            vo.setPrice();
            vo.setIdCard(DesensitizedUtil.idCardNum(vo.getIdCard(), 6, 4));
        });

        return new PageUtils<>(resp, page.getTotal());
    }

    public HwzOrderDetailDTO orderDetail(Long id) {
        MbrOrder order = Optional.ofNullable(this.getById(id)).orElseThrow(() -> new BaseException(-1, "订单不存在"));
        HwzOrderDetailDTO vo = HuiWanZuApiUtil.getOrderById(order.getThirdOrderId());
        if (vo != null) {
            vo.setOrderLogList(orderLogService.listAllLogsByOrderId(OrderLogQueryReq.builder().orderId(id).build()));
        }
        return vo;
    }


    @Transactional(rollbackFor =  Exception.class)
    public void markAbnormal(MbrOrderMarkReq req) {

        boolean success = this.lambdaUpdate()
                .set(MbrOrder::getStatus, MbrOrderStatusEnum.ABNORMAL.getCode())
                .eq(MbrOrder::getId, req.getId())
                .eq(AbstractBaseEntity::getDeleted, false)
                .ne(MbrOrder::getStatus, MbrOrderStatusEnum.ABNORMAL.getCode())
                .update(new MbrOrder());
        if (success) {
            orderLogService.addLog(ShiroUtils.getUserId(),
                    req.getId(),
                    MbrOrderStatusEnum.ABNORMAL.getCode(),
                    MbrOrderStatusEnum.ABNORMAL.getCode(),
                    MbrOrderStatusEnum.ABNORMAL.getDesc(),
                    req.getRemark());
        }
    }

}