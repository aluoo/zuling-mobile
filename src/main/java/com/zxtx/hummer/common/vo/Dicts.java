package com.zxtx.hummer.common.vo;

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
import java.math.BigDecimal;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("sys_dict")
public class Dicts implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("参数")
    private String name;

    @ApiModelProperty("值")
    private String value;

    private BigDecimal sort;

    private String type;
}