package com.zxtx.hummer.em.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @create 2023/9/13 9:35
 * @desc 员工上下线操作变更记录
 **/
@Data
@ApiModel(value = "员工上下线操作变更记录", description = "员工上下线操作变更记录")
public class OperateLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @IgnoreExcelField
    private Long id;

    @IgnoreExcelField
    private Long employeeId;

    @ApiModelProperty("变更时间")
    @ExcelField(name = "变更时间", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ExcelField(name = "类型", replace = {"1_上线", "2_下线"})
    @ApiModelProperty("1上线2下线")
    private Integer type;

    @ExcelField(name = "负责BD")
    @ApiModelProperty("BD名称")
    private String bdName;

    @ExcelField(name = "员工姓名")
    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ExcelField(name = "员工手机号")
    @ApiModelProperty("员工手机号")
    private String mobileNumber;

    @ExcelField(name = "渠道名称")
    @ApiModelProperty("渠道名称")
    private String companyName;


    @ExcelField(name = "部门名称")
    @ApiModelProperty("部门名称")
    private String deptName;

    @ExcelField(name = "部门管理员")
    @ApiModelProperty("部门管理员")
    private String deptManageName;

    @ExcelField(name = "管理员手机号")
    @ApiModelProperty("管理员手机号")
    private String deptManageMobile;

    @ExcelField(name = "原因", replace = {"1_虚假宣传", "2_其他"})
    @ApiModelProperty("1虚假宣传2其他")
    private Integer reason;

    @ExcelField(name = "备注")
    @ApiModelProperty("备注")
    private String remark;

    @ExcelField(name = "操作人")
    @ApiModelProperty("创建人")
    private String creator;

}