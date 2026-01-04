package com.zxtx.hummer.exchange.domain.dto;

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
public class ExchangeEmployeePackageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("代理ID")
    private Long employeeId;

    @ApiModelProperty("换机包ID")
    private List<Long> exchangePhoneIds;



}