package com.zxtx.hummer.commission.dto;

import com.zxtx.hummer.commission.domain.CommissionPlan;
import com.zxtx.hummer.commission.domain.CommissionPlanConf;
import com.zxtx.hummer.commission.domain.CommissionPlanMembers;
import lombok.Data;

import java.util.List;

@Data
public class CmPlanLogDTO {
    private CommissionPlan plan;

    private List<CommissionPlanMembers> members;

    private List<CommissionPlanConf> issueConfs;
}
