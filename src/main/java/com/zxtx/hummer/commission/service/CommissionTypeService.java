package com.zxtx.hummer.commission.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.commission.dao.mapper.CommissionPlanMapper;
import com.zxtx.hummer.commission.dao.mapper.CommissionTypeMapper;
import com.zxtx.hummer.commission.domain.CommissionPlan;
import com.zxtx.hummer.commission.domain.CommissionType;
import com.zxtx.hummer.commission.dto.OverviewDTO;
import com.zxtx.hummer.commission.req.CommissionQueryReq;
import com.zxtx.hummer.common.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class CommissionTypeService extends ServiceImpl<CommissionTypeMapper, CommissionType> {
    @Autowired
    CommissionPlanMapper commissionPlanMapper;

    public List<OverviewDTO> overview(Long employeeId) {
        List<OverviewDTO> resultList = new ArrayList<>();
        List<CommissionType> bizTypeList = this.list(
                Wrappers.lambdaQuery(CommissionType.class).eq(CommissionType::getDeleted, 0));
        if (CollUtil.isEmpty(bizTypeList)) {
            return resultList;
        }

        for (CommissionType type : bizTypeList) {
            OverviewDTO overviewDTO = new OverviewDTO();
            long num = commissionPlanMapper.selectCount(Wrappers.lambdaQuery(CommissionPlan.class)
                    .eq(CommissionPlan::getEmployeeId, employeeId)
                    .eq(CommissionPlan::getTypeId, type.getId()));
            overviewDTO.setBizTypeId(type.getId());
            overviewDTO.setBizTypeName(type.getName());
            overviewDTO.setNum((int) num);
            resultList.add(overviewDTO);
        }
        return resultList;
    }

    public PageUtils<CommissionType> list(CommissionQueryReq req) {
        Page<Object> page = PageHelper.startPage(req.getPage(), req.getPageSize());
        List<CommissionType> list = this.lambdaQuery()
                .eq(CommissionType::getDeleted, false)
                .orderByDesc(CommissionType::getCreateTime)
                .list();
        if (CollUtil.isEmpty(list)) {
            return PageUtils.emptyPage();
        }
        return new PageUtils<>(list, page.getTotal());
    }

}