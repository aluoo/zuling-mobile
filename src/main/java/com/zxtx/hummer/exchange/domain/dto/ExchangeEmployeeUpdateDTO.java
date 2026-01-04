package com.zxtx.hummer.exchange.domain.dto;

import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangeEmployeeUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("代理ID")
    private Long employeeId;

    @ApiModelProperty("代理名称")
    private String employeeName;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("负责地区逗号分割中文")
    private String address;

    @ApiModelProperty("开通业务逗号分割中文")
    private String business;

    @ApiModelProperty("平台审核")
    private Boolean platCheck;


}