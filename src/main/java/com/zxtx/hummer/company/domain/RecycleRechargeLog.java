package com.zxtx.hummer.company.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @author chenjian
 * @Description
 * @Date 2024/3/11
 * @Copyright
 * @Version 1.0
 */
@Data
@TableName("recycle_recharge_log")
public class RecycleRechargeLog extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "唯一标识", hidden = true)
    private Long id;
    @ApiModelProperty(value = "是否删除")
    private Boolean deleted;
    @ApiModelProperty(value = "回收商Id")
    private Long companyId;
    @ApiModelProperty(value = "订单状态")
    private Integer status;
    @ApiModelProperty(value = "打款凭证")
    private String imageUrl;
    @ApiModelProperty(value = "充值金额")
    private Long rechargeAmount;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "创建人")
    private Long createBy;
    @ApiModelProperty(value = "更新人")
    private Long updateBy;
}