package com.zxtx.hummer.exchange.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class ExchangeInstallReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("安装包名称")
    private String name;

    @ApiModelProperty("1正常0禁用")
    private Integer status;

    @ApiModelProperty("渠道号")
    private String channelNo;

    @ApiModelProperty("渠道Token")
    private String channelToken;

    @ApiModelProperty("包编码")
    private String type;
}