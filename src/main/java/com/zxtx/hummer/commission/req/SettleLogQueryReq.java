package com.zxtx.hummer.commission.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SettleLogQueryReq {


    @ApiModelProperty("页数")
    private int page = 1;

    @ApiModelProperty("每页条数")
    private int pageSize = 10;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("订单Id")
    private String orderId;

    @ApiModelProperty("关联备注")
    private String remark;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("0-不结算、1-待结算、2-已结算")
    private Integer settleStatus;

    @ApiModelProperty("业务类型")
    private Integer commissionType;

    @ApiModelProperty("合伙人名字")
    private String bdName;
    @ApiModelProperty("合伙人手机")
    private String bdMobile;

    private String ancestors;

    private Long areaId;
    private Long agentId;
    private Long chainCompanyId;

    private String chainCompanyMobile;
    private String companyMobile;
}