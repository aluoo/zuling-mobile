package com.zxtx.hummer.em.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;


@Data
public class EmCfgLsRps {

    private Long id;

    @ApiModelProperty("渠道公司id")
    private Long companyId;

    @ApiModelProperty("渠道公司")
    private String companyName;

    private Long empId;

    @ApiModelProperty("员工名")
    private String empName;

    @ApiModelProperty("手机号")
    private String mobileNumber;

    @ApiModelProperty("对应编号")
    private String pairNo;

    @ApiModelProperty("内部编码")
    private String interNo;

    @ApiModelProperty("内部员工名")
    private String interName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy.MM.dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy.MM.dd HH:mm:ss")
    private Date updateTime;

    private String createTimeStr;

    public String getCreateTimeStr() {
        if (createTime == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(createTime);
    }
}
