package com.zxtx.hummer.withdraw.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 提现审核记录
 * </p>
 *
 * @author chenjian
 * @since 2023/06/02
 */
@Getter
@Setter
@TableName("withdraw_check_log")
@ApiModel(value = "WithdrawCheckLog对象", description = "提现审核记录")
public class WithdrawCheckLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("提现申请id")
    private Long applyId;

    @ApiModelProperty("审核人id")
    private Long sysUserId;

    @ApiModelProperty("审核人id")
    private Long employeeId;

    @ApiModelProperty("归属审核人id(平台审核类型为渠道子后台时有值)")
    private Long toEmployeeId;

    @ApiModelProperty("审核前状态")
    private Integer oldStatus;

    @ApiModelProperty("审核后状态")
    private Integer newStatus;

    @ApiModelProperty("审核备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
