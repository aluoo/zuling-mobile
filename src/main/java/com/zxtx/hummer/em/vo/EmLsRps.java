package com.zxtx.hummer.em.vo;

import com.zxtx.hummer.common.annotation.ExcelField;
import com.zxtx.hummer.common.annotation.IgnoreExcelField;
import com.zxtx.hummer.common.utils.StringUtils;
import lombok.Data;

@Data
public class EmLsRps {


    @IgnoreExcelField
    private Long id;

    @ExcelField(name = "渠道公司")
    private String companyName;


    @ExcelField(name = "渠道公司状态", replace = {"1_待审核", "2_正常", "3_审核失败", "4_注销", "5_下线"})
    private Integer companyStatus;


    @ExcelField(name = "部门名称")
    private String deptName;


    @ExcelField(name = "部门状态", replace = {"1_正常", "2_已注销", "3_下线"})
    private Integer deptStatus;


    @ExcelField(name = "部门管理员")
    private String mDeptMan;

    @ExcelField(name = "管理员手机号")
    private String mDeptManMobile;


    @ExcelField(name = "员工姓名")
    private String employeeName;

    @ExcelField(name = "员工手机号")
    private String mobileNumber;

    @ExcelField(name = "钱包可用余额")
    private Long ableBalance;

    @ExcelField(name = "员工状态", replace = {"1_正常", "2_已注销", "3_下线"})
    private Integer status;

    @IgnoreExcelField
    private Long deptId;

    @IgnoreExcelField
    private String ancestors;

    public String getAbleBalance() {
        if (this.ableBalance == null) {
            this.ableBalance = 0L;
        }
        return StringUtils.decimalFormat(Double.valueOf(this.ableBalance));
    }


}