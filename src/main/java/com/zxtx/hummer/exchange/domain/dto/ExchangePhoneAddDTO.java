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
public class ExchangePhoneAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("换机包名称")
    private String name;

    @ApiModelProperty("使用场景")
    private Integer type;

    @ApiModelProperty("包编码")
    private String channelNo;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("安装包ID集合")
    private List<Long> installIds;

    @ApiModelProperty("关联合伙人集合")
    private List<Long> employeeIds;




}