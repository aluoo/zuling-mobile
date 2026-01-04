package com.zxtx.hummer.insurance.req;

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
public class DiInsuranceOrderReq {

    private int page = 1;

    private int pageSize = 10;

    @ApiModelProperty("投保单号")
    private Long id;

    @ApiModelProperty("IMEI")
    private String imeiNo;

    @ApiModelProperty("店员手机号")
    private String storePhone;

    @ApiModelProperty("顾客姓名")
    private String customName;

    @ApiModelProperty("顾客手机号")
    private String customPhone;

    @ApiModelProperty("产品名称")
    private String insuranceName;

    @ApiModelProperty("-2退款中-1已关闭0待支付1待审核2已完成")
    private Integer status;

    @ApiModelProperty("0待支付1资料待审核2退款待审核3待上传4待出保5已出保6资料审核失败7退款审核失败8手动取消9自动取消10已退款")
    private Integer subStatus;

    @ApiModelProperty("支付日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payStartTime;

    @ApiModelProperty("支付日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payEndTime;

    @ApiModelProperty("提交日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyStartTime;

    @ApiModelProperty("提交日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyEndTime;

}