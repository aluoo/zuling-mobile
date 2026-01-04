package com.zxtx.hummer.exchange.vo;

import com.zxtx.hummer.exchange.domain.MbExchangePhone;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangeEmployeeViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("代理ID")
    private Long employeeId;

    @ApiModelProperty("门店数量")
    private Integer companyNum;

    @ApiModelProperty("员工数")
    private Integer staffNum;

    @ApiModelProperty("员工余额")
    private Long ableBalance;

    @ApiModelProperty("代理名称")
    private String employeeName;

    @ApiModelProperty("代理手机号码")
    private String employeePhone;

    @ApiModelProperty("合伙人名称")
    private String bdName;

    @ApiModelProperty("身份证号码")
    private String idCard;

    @ApiModelProperty("代理级别1合伙人2区域经理3代理")
    private Integer level;

    @ApiModelProperty("负责地区")
    private String address;

    @ApiModelProperty("已开通业务")
    private String business;

    @ApiModelProperty("平台审核")
    private Boolean platCheck;

    @ApiModelProperty("换机包列表")
    List<MbExchangePhone> packageList;








}