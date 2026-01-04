package com.zxtx.hummer.commission.service;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxtx.hummer.commission.domain.CommissionPlan;
import com.zxtx.hummer.commission.domain.CommissionPlanLog;
import com.zxtx.hummer.commission.dto.CmPlanLogDTO;
import com.zxtx.hummer.commission.dao.mapper.CommissionPlanLogMapper;
import com.zxtx.hummer.commission.dao.mapper.CommissionPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author peng can
 * @date 2022/12/1
 */
@Service
public class CommissionPlanLogService extends ServiceImpl<CommissionPlanLogMapper, CommissionPlanLog> {

    @Autowired
    CommissionPlanMapper commissionPlanMapper;
    @Autowired
    CommissionPlanConfService planConfService;
    @Autowired
    CommissionPlanMembersService planMembersService;

    public void backUpPlan(Long planId) {
        //保存到方案历史记录
        CmPlanLogDTO logDTO = new CmPlanLogDTO();
        CommissionPlan plan = commissionPlanMapper.selectById(planId);
        logDTO.setPlan(plan);
        logDTO.setIssueConfs(planConfService.queryByPlanId(planId));
        logDTO.setMembers(planMembersService.queryByPlanId(planId));
        CommissionPlanLog commissionPlanLog = new CommissionPlanLog();
        commissionPlanLog.setPlanId(plan.getId());
        commissionPlanLog.setEmployeeId(plan.getEmployeeId());
        JSONConfig jsonConfig = JSONConfig.create();
        jsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        commissionPlanLog.setContent(JSONUtil.toJsonStr(logDTO, jsonConfig));
        this.save(commissionPlanLog);
    }


}