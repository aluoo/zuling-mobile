package com.zxtx.hummer.dept.vo;


import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.exception.BusinessException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class CreateDeptReq {
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("管理员名称")
    private String managerName;
    @ApiModelProperty("管理员手机号")
    private String mobile;
    @ApiModelProperty("父部门id")
    private Long pdeptId;

    public void setMobile(String mobile) {
        if ((StringUtils.isNotBlank(mobile) && mobile.length() != 11)) {
            throw new BusinessException(BizError.MOBILE_LENGTH_ERROR);
        }
        this.mobile = mobile;
    }
}
