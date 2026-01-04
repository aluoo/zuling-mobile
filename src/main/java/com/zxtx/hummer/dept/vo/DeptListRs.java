package com.zxtx.hummer.dept.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeptListRs {
    @ApiModelProperty("部门id")
    private Long id;
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("部门code")
    private String code;
    @ApiModelProperty("部门类型 1管理部门 2普通部门")
    private Integer type;
    @ApiModelProperty("公司id")
    private Long companyId;
    @ApiModelProperty("父部门id")
    private Long pDeptId;

    public DeptListRs getObject() {
        return this;
    }
}
