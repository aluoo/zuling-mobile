package com.zxtx.hummer.insurance.req;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class DiInsuranceFixReq extends AbstractBaseQueryEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("保险单编号")
    private Long fixId;
    @ApiModelProperty("投保单编号")
    private Long orderId;
    @ApiModelProperty("服务单编号")
    private String orderInsuranceNo;
    @ApiModelProperty("顾客手机号")
    private String customPhone;
    @ApiModelProperty("顾客姓名")
    private String customName;
    @ApiModelProperty("维修城市")
    private String fixCity;
    @ApiModelProperty("0_报险审核中1_资料审核中2_审核通过,待上传维修资料3_审核失败,请修改报修资料4_资料审核通过,待理赔5_资料审核失败,修改维修资料6_理赔完成7_报险单取消")
    private Integer status;

    @ApiModelProperty("数保产品类型")
    private String insuranceType;

    @ApiModelProperty("理赔服务选项ID")
    private Long serviceType;
    @ApiModelProperty("理赔项目选项ID")
    private Long claimItem;

    @ApiModelProperty("提交日期开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;
    @ApiModelProperty("提交日期结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    @ApiModelProperty("imei")
    private String imeiNo;
}