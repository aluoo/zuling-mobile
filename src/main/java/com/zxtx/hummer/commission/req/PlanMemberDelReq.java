package com.zxtx.hummer.commission.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PlanMemberDelReq {

    @ApiModelProperty("佣金方案ID")
    private Long planId;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("删除的员工ID集合")
    private List<Long> delMembers;


}
