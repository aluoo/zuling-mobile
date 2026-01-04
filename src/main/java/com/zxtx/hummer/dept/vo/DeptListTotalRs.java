package com.zxtx.hummer.dept.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptListTotalRs extends DeptListRs {
    @ApiModelProperty("总人数")
    private Integer totalPerson;
}
