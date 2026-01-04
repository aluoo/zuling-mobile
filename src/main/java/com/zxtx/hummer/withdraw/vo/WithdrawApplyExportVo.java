package com.zxtx.hummer.withdraw.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 套餐信息Vo对象
 **/
@Data
@ApiModel(value = "PackageInfo对象", description = "套餐信息")
public class WithdrawApplyExportVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("提现单号")
    private String applyNo;

    @ApiModelProperty("账号")
    private String accountNo;

    @ApiModelProperty("姓名")
    private String ownerName;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("收款人手机号码")
    private String mobileNumber;

    @ApiModelProperty("体现收入元")
    private String inAmountYuan;


}