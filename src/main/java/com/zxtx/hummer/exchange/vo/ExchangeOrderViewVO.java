package com.zxtx.hummer.exchange.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.exchange.domain.MbExchangeCustom;
import com.zxtx.hummer.exchange.domain.MbExchangePic;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangeOrderViewVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long id;

    @ApiModelProperty("门店")
    private String storeName;

    @ApiModelProperty("店员姓名")
    private String storeEmployee;

    @ApiModelProperty("手机号码")
    private String storeMobile;

    @ApiModelProperty("所属代理")
    private String agentName;

    @ApiModelProperty("所属区域经理")
    private String areaName;

    @ApiModelProperty("合伙人")
    private String bdName;

    @ApiModelProperty("顾客手机号")
    private String customMobile;

    @ApiModelProperty("手机系统")
    private String sysMobile;

    @ApiModelProperty("IMEI码")
    private String imeiNo;

    @ApiModelProperty("包编码")
    private String exchangePhoneNo;

    @ApiModelProperty("提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date trialTime;

    @ApiModelProperty("审核状态-1失败0待审核1系统通过2人工通过")
    private Integer status;

    @ApiModelProperty("结算状态0未结算1已结算")
    private Integer settleStatus;

    @ApiModelProperty("晒图模块")
    List<MbExchangePic> picList;

    @ApiModelProperty("安装包列表")
    List<MbExchangeCustom> installList;

    @ApiModelProperty("操作日志")
    List<OrderLogDTO> orderLogList;
    @ApiModelProperty("开机时长(秒)")
    private Integer openTime;
    @ApiModelProperty("门店已晒单数")
    private Long companyNum;
    @ApiModelProperty("店员已晒单数")
    private Long storeNum;
    @ApiModelProperty("红色预警")
    private Boolean redFlag;
    @ApiModelProperty("品牌")
    private String brand;
    @ApiModelProperty("oaid")
    private String oaid;

    private List<OrderLogDTO> historyOrders;

    @ApiModelProperty("备注")
    private String remark;
}