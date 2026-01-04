package com.zxtx.hummer.withdraw.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 审核异常对象
 **/
@Data
@ApiModel(value = "审核异常对象")
public class WithdrawCardIlegalTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String legalCode;

    private String legalName;


}