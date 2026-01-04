package com.zxtx.hummer.commission.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/6/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("commission_settle_data_daily_base")
@ApiModel(value = "CommissionSettleDataDailyBase对象", description = "佣金统计每日基础表")
public class CommissionSettleDataDailyBase implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("每天0点")
    private Date day;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("结算业务类型(1-推广、2-服务费、3-奖金)")
    private Integer bizType;

    @ApiModelProperty("获得类型")
    private Integer gainType;

    @ApiModelProperty("统计金额(分)")
    private Integer value;

    private Date createTime;
    private Date updateTime;
}