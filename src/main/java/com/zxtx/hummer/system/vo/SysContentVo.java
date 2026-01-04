package com.zxtx.hummer.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenjian
 * @create 2023/7/17 15:14
 * @desc 公告对象
 **/
@Data
@ApiModel(value = "", description = "公告对象")
public class SysContentVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty("公告标题")
    private String title;

    @ApiModelProperty("1重要2普通")
    private Integer type;

    @ApiModelProperty("内容详情")
    private String content;

    @ApiModelProperty("上传文件地址")
    private String fileUrl;

    @ApiModelProperty("发布日期")
    private Date pulishDate;

    @ApiModelProperty("1待发布2已发布3已停用")
    private Integer status;

    @ApiModelProperty("0否1是")
    private Integer deleted;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}