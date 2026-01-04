package com.zxtx.hummer.exchange.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class ExchangeOrderReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("代理名称")
    private String agentName;

    @ApiModelProperty("门店名称")
    private String companyName;

    @ApiModelProperty("连锁总店名称")
    private String chainCompanyName;

    @ApiModelProperty("店员名称")
    private String storeEmployeeName;

    @ApiModelProperty("店员手机号")
    private String storeEmployeePhone;

    @ApiModelProperty("合伙人名称")
    private String bdName;

    @ApiModelProperty("区域经理名称")
    private String areaName;

    @ApiModelProperty("IMEI")
    private String imeiNo;

    @ApiModelProperty("oaid")
    private String oaid;

    @ApiModelProperty("换机包编码")
    private String exchangePhoneNo;

    @ApiModelProperty("4一键安装3换机5快手绿洲")
    private Integer type;

    @ApiModelProperty("来源0外部吉迅1正常晒单2晒单跳过")
    private Integer source;

    @ApiModelProperty("审核状态-1失败0待审核1系统通过2人工通过")
    private Integer status;

    @ApiModelProperty("提交日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("提交日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("审核日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trialStartTime;

    @ApiModelProperty("审核日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trialEndTime;

    @ApiModelProperty("门店ID集合")
    private Set<Long> cmpIds;

    private Long id;
    private String customPhone;
    private String employeePhone;

    private String orderSn;

    private Long bdId;
    private Long areaId;
    private Long agentId;
    private Long chainCompanyId;

    private String chainCompanyMobile;
    private String companyMobile;

    // 子后台审核状态，不显示机审
    private List<Integer> specialStatus;

    @ApiModelProperty("平台审核")
    private Boolean platCheck;

    @ApiModelProperty("异常标识")
    private Boolean illegal;
}