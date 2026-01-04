package com.zxtx.hummer.em.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class EmCfgLsReq {

    @NotNull
    private Integer emCfgType;

    @ApiModelProperty("公司名")
    private String companyName;

    @ApiModelProperty("手机号")
    private String mobileNumber;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private Date ctStart;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private Date ctEnd;


}
