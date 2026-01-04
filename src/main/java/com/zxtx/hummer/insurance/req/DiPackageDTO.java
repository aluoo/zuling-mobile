package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class DiPackageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("1正常2下线")
    private Integer status;




}