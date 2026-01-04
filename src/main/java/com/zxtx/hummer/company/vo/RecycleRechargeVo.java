package com.zxtx.hummer.company.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 回收商充值
 */
@Data
@ApiModel(value = "回收商充值对象", description = "回收商充值")
public class RecycleRechargeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("回收商名称")
    private String companyName;

    @ApiModelProperty("充值金额(分)")
    private Long rechargeAmount;

    @ApiModelProperty("充值金额(元)")
    private String rechargeAmountStr;

    @ApiModelProperty("打款凭证")
    private String imageUrl;

    @ApiModelProperty("状态0待审核1拒绝2通过")
    private Integer status;

    @ApiModelProperty("状态中文")
    private String statusName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新人")
    private String updateBy;


}