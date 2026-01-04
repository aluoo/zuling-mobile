package com.zxtx.hummer.em.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 员工上下线操作日志表
 * </p>
 *
 * @author L
 * @since 2023-09-12
 */
@Getter
@Setter
@TableName("employee_operate_log")
@ApiModel(value = "EmployeeOperateLog对象", description = "员工上下线操作日志表")
public class EmployeeOperateLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("1上线2下线")
    private Integer type;

    @ApiModelProperty("BD名称")
    private String bdName;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("员工手机号")
    private String mobileNumber;

    @ApiModelProperty("渠道名称")
    private String companyName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门管理员")
    private String deptManageName;

    @ApiModelProperty("管理员手机号")
    private String deptManageMobile;

    @ApiModelProperty("1虚假宣传2其他")
    private Integer reason;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("凭证图片地址")
    private String downloadUrl;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("是否删除 0否1是")
    private Boolean deleted;
}
