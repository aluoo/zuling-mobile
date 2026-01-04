package com.zxtx.hummer.withdraw.req;

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
public class WithdrawApplyReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("申请状态(-1 失败, 0 待审核,1 审核通过，待打款 2 打款成功)")
    private Integer status;

    @ApiModelProperty("类型(1-银行卡、2-支付宝)")
    private Integer type;

    @ApiModelProperty("0后台1渠道子后台")
    private Integer toPlatform;

    @ApiModelProperty("门店名称")
    private String channelName;
}
