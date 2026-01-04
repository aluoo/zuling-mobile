package com.zxtx.hummer.dept.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptRes {
    @ApiModelProperty("是否并入老部门 是为true")
    private boolean addToOldDept = false;
    @ApiModelProperty("提醒信息")
    private String remind;
}
