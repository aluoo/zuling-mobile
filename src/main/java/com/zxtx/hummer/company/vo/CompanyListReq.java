package com.zxtx.hummer.company.vo;

import com.zxtx.hummer.common.vo.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CompanyListReq extends PageReq {

    @ApiModelProperty(value = "门店负责人手机号码")
    private String mobileNumber;

    @ApiModelProperty(value = "门店名称")
    private String companyName;

    private String deptCodes;

    @ApiModelProperty(value = "门店类型1-公司,2-门店3连锁4服务商")
    private Integer type;

    @ApiModelProperty(value = "门店状态1-待审核， 2-正常， 3-审核失败， 4-注销，5-下线")
    private Integer status;

    @ApiModelProperty("审核日期开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("审核日期结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
