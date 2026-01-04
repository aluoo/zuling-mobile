package com.zxtx.hummer.company.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zxtx.hummer.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Company extends BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("上级公司id,-1表示最上层公司")
    private Long pId;

    @ApiModelProperty("1-公司,2-门店 3连锁 4服务商")
    private Integer type;

    @ApiModelProperty("门店名称")
    private String name;

    @ApiModelProperty("公司code")
    private String code;

    @ApiModelProperty("状态（1-待审核， 2-正常， 3-审核失败， 4-注销，5-下线）")
    private Byte status;

    @ApiModelProperty("负责人")
    private String contact;

    @ApiModelProperty("联系人电话")
    private String contactMobile;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String region;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("省编码")
    private String provinceCode;

    @ApiModelProperty("市编码")
    private String cityCode;

    @ApiModelProperty("区编码")
    private String regionCode;

    @ApiModelProperty("营业执照")
    private String frontUrl;

    @ApiModelProperty("营业执照")
    private String busLicense;

    @ApiModelProperty("身份证照片-正面")
    private String idUrlUp;

    @ApiModelProperty("身份证照片-反面")
    private String idUrlDown;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("身份证名字")
    private String idName;

    @ApiModelProperty("发起申请人ID")
    private Long aplId;

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("用户表ID,关联OPENID用")
    private Long userId;

    @ApiModelProperty("是否可开票")
    private Boolean invoiceAble;

    @ApiModelProperty("是否可登录设置")
    private Boolean loginAble;

    @ApiModelProperty("是否佣金系统")
    private Boolean commissionAble;

    @ApiModelProperty("父部门ID")
    private Long pDeptId;

    @ApiModelProperty("创建人")
    private String creator;

    @ApiModelProperty("更新人")
    private String updator;

    @ApiModelProperty(value = "拉新模式")
    private Integer exchangeType;
}