package com.zxtx.hummer.insurance.req;

import com.zxtx.hummer.common.domain.AbstractBaseQueryEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/17
 * @Copyright
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class DiInsuranceFixOrderSettlementQueryReq extends AbstractBaseQueryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("结算单号")
    private String applyNo;
    @ApiModelProperty("报险单号")
    private Long fixOrderId;
    @ApiModelProperty("收款人")
    private String ownerName;
    @ApiModelProperty("收款账号")
    private String accountNo;
    @ApiModelProperty("报险人")
    private String fixOrderCreatorName;
    @ApiModelProperty("报险人手机号")
    private String fixOrderCreatorPhone;
    @ApiModelProperty("所属门店名称")
    private String storeCompanyName;

    @ApiModelProperty("状态 -1打款失败 0待打款 2打款成功")
    private Integer status;

    @ApiModelProperty("提交日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;

    @ApiModelProperty("提交日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
}