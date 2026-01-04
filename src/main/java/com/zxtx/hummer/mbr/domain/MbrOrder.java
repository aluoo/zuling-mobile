package com.zxtx.hummer.mbr.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import com.zxtx.hummer.mbr.domain.enums.MbrOrderStatusEnum;
import com.zxtx.hummer.mbr.domain.enums.MbrOrderSubStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author chenjian
 * @since 2024-06-05
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("mbr_order")
@ApiModel(value = "租机单表")
@Data
public class MbrOrder extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("门店员工ID")
    private Long storeEmployeeId;
    @ApiModelProperty("门店ID")
    private Long storeCompanyId;
    @ApiModelProperty("第三方订单号")
    private Long thirdOrderId;
    @ApiModelProperty("手机商品名称")
    private String productName;
    @ApiModelProperty("手机规格名称")
    private String productSpec;
    @ApiModelProperty("1新机2二手机")
    private String productType;
    @ApiModelProperty("期数")
    private Integer period;
    @ApiModelProperty("客户姓名")
    private String customName;
    @ApiModelProperty("客户手机号")
    private String customPhone;
    @ApiModelProperty("客户身份证")
    private String idCard;
    /**
     * @see MbrOrderStatusEnum
     */
    @ApiModelProperty("订单状态")
    private Integer status;
    /**
     * @see MbrOrderSubStatusEnum
     */
    @ApiModelProperty("订单子状态状态")
    private Integer subStatus;


    @ApiModelProperty("商品成本")
    private Long settleAmount;
    @ApiModelProperty("方案价格")
    private Long planAmount;
    @ApiModelProperty("押金价格")
    private Long depositAmount;

}