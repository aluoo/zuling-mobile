package com.zxtx.hummer.company.req;

import com.zxtx.hummer.common.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/25
 * @Copyright
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(name = "门店名称")
    @NotBlank(message = "门店名称不能为空")
    private String name;

    @ExcelField(name = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    @ExcelField(name = "管理员")
    @NotBlank(message = "管理员不能为空")
    private String deptManager;

    @ExcelField(name = "管理员手机号")
    @NotBlank(message = "管理员手机号不能为空")
    private String deptManagerMobile;

    @ExcelField(name = "员工姓名")
    @NotBlank(message = "员工姓名不能为空")
    private String employeeName;

    @ExcelField(name = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String employeeMobile;
}