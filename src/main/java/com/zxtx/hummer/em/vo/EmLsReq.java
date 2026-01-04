package com.zxtx.hummer.em.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EmLsReq {

    @ApiModelProperty("渠道公司")
    private String companyName;

    @ApiModelProperty("员工手机号")
    private String mobileNumber;
    private String employeeName;

    private Integer status;

    private Integer companyStatus;

    private Integer deptStatus;

    private String deptCodes;

    private String deptName;

}
