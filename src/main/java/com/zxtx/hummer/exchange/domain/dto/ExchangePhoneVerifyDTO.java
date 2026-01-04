package com.zxtx.hummer.exchange.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chenjian
 * @create 2023/5/22 13:52
 * @desc 代理对象
 **/
@Data
public class ExchangePhoneVerifyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("审核结果1通过-1不通过")
    private Integer status;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("通过时必填")
    private String imeiNo;

    @ApiModelProperty("图片对象")
    private List<PicDTO> picList;

    @Data
    public static class PicDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        @ApiModelProperty("id")
        private Long id;
        @ApiModelProperty("uid")
        private String uid;
        @ApiModelProperty("did")
        private String did;

    }




}