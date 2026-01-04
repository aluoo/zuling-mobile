package com.zxtx.hummer.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 数保门店账户充值记录表
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Getter
@Setter
@TableName("di_company_recharge_log")
@ApiModel(value = "DiCompanyRechargeLog对象", description = "数保门店账户充值记录表")
public class DiCompanyRechargeLog extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("门店ID")
    private Long companyId;

    @ApiModelProperty("充值金额")
    private Long rechargeAmount;

    @ApiModelProperty("打款凭证")
    private String imageUrl;

    @ApiModelProperty("0待审核1拒绝2通过")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;
}
