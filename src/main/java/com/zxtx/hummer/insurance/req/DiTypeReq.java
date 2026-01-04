package com.zxtx.hummer.insurance.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class DiTypeReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("安装包名称")
    private String name;

    @ApiModelProperty("1正常2下线3删除")
    private Integer status;

    @ApiModelProperty("1是0否")
    private Boolean packageAble;
}
