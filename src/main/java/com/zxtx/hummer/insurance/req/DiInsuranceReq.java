package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class DiInsuranceReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("产品名称")
    private String name;

    @ApiModelProperty("产品类型")
    private Long typeId;

    @ApiModelProperty("套餐")
    private Long packageId;

    @ApiModelProperty("1正常2下线")
    private Integer status;

    @ApiModelProperty("手机产品ID")
    private Long productId;

    private List<Long> insuranceIds;
}
