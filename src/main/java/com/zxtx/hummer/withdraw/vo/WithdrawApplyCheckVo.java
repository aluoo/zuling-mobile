package com.zxtx.hummer.withdraw.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 套餐信息Vo对象
 **/
@Data
@ApiModel(value = "PackageInfo对象", description = "套餐信息")
public class WithdrawApplyCheckVo implements Serializable {

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

    @ApiModelProperty("提现单号")
    private String applyNo;

    @ApiModelProperty("审核人名称")
    private String userName;


}