package com.zxtx.hummer.mbr.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @Description
 * @Date 2025/5/6
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MbrOrderReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("订单号")
    private Long id;
    @ApiModelProperty("第三方订单号")
    private Long thirdOrderId;

    @ApiModelProperty("客户姓名")
    private String customName;

    @ApiModelProperty("门店名称")
    private String storeCompanyName;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer status;


}