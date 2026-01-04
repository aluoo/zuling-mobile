package com.zxtx.hummer.exchange.req;

import com.zxtx.hummer.common.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class ExchangeEmployeeInfoReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("代理名称")
    private String employeeName;

    @ApiModelProperty("代理手机号")
    private String employeePhone;

    @ApiModelProperty("合伙人层级")
    private String bdAncestor;

    @ApiModelProperty("1合伙人2区域经理3代理")
    private Integer level;

    @ApiModelProperty("1正常2注销3下线")
    private Integer status;

    @ApiModelProperty("合伙人手机号")
    private String bdPhone;
}
