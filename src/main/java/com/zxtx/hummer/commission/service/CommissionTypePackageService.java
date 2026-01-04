package com.zxtx.hummer.commission.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.commission.dao.mapper.CommissionTypePackageMapper;
import com.zxtx.hummer.commission.domain.CommissionPlan;
import com.zxtx.hummer.commission.domain.CommissionPlanConf;
import com.zxtx.hummer.commission.domain.CommissionType;
import com.zxtx.hummer.commission.domain.CommissionTypePackage;
import com.zxtx.hummer.commission.req.CommissionQueryReq;
import com.zxtx.hummer.commission.req.CommissionTypePackageReq;
import com.zxtx.hummer.commission.rsp.CommissionTypePackageVO;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class CommissionTypePackageService extends ServiceImpl<CommissionTypePackageMapper, CommissionTypePackage> {

    @Autowired
    private CommissionTypeService commissionTypeService;
    @Autowired
    private CommissionPlanService commissionPlanService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CommissionPlanConfService commissionPlanConfService;

    public PageUtils<CommissionTypePackageVO> list(CommissionQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<CommissionTypePackage> list = this.lambdaQuery()
                .eq(CommissionTypePackage::getDeleted, false)
                .eq(ObjectUtil.isNotNull(req.getTypeId()), CommissionTypePackage::getTypeId, req.getTypeId())
                .orderByDesc(CommissionTypePackage::getCreateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        List<CommissionTypePackageVO> resp = BeanUtil.copyToList(list, CommissionTypePackageVO.class, CopyOptions.create().ignoreNullValue());
        Set<Long> typeIds = resp.stream().map(CommissionTypePackageVO::getTypeId).collect(Collectors.toSet());

        Map<Long, CommissionType> commissionTypeMap = commissionTypeService.lambdaQuery()
                .in(CommissionType::getId, typeIds).list().stream().filter(Objects::nonNull)
                .collect(Collectors.toMap(CommissionType::getId, Function.identity()));

        resp.forEach(o -> {
            o.setTypeName(Optional.ofNullable(commissionTypeMap.get(o.getTypeId())).map(CommissionType::getName).orElse(null));
        });

        return new PageUtils<>(resp, page.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    public void savePackage(CommissionTypePackageReq req) {

        CommissionTypePackage commissionTypePackage = new CommissionTypePackage();
        BeanUtil.copyProperties(req, commissionTypePackage);
        commissionTypePackage.setDeleted(false);
        commissionTypePackage.setMaxCommissionFee(req.getMaxCommissionFee().multiply(new BigDecimal(100)).longValue());
        this.save(commissionTypePackage);

        List<CommissionPlan> planList = commissionPlanService.lambdaQuery()
                .eq(CommissionPlan::getTypeId,req.getTypeId())
                .list();

        for (CommissionPlan planItem: planList) {
            Long planId = planItem.getId();
            Long employeeId = planItem.getEmployeeId();

            Employee employee  = employeeService.getById(employeeId);

            long count  = commissionPlanConfService.lambdaQuery()
                    .eq(CommissionPlanConf::getPlanId,planId)
                    .eq(CommissionPlanConf::getTypePackageId,commissionTypePackage.getId())
                    .count();

            if (count<1){
                CommissionPlanConf planIssueConf = new CommissionPlanConf();
                planIssueConf.setPlanId(planId);
                planIssueConf.setTypePackageId(commissionTypePackage.getId());
                planIssueConf.setSuperDivide(0L);
                planIssueConf.setChildDivide(0L);
                planIssueConf.setSelfDivide(0L);
                planIssueConf.setAncestors(employee.getAncestors());
                planIssueConf.setLevel(employee.getLevel());
                planIssueConf.setCreateTime(new Date());
                planIssueConf.setUpdateTime(new Date());
                planIssueConf.setSuperScale(BigDecimal.ZERO);
                planIssueConf.setChildScale(BigDecimal.ZERO);
                planIssueConf.setSelfScale(BigDecimal.ZERO);
                commissionPlanConfService.save(planIssueConf);
            }
        }
    }
}