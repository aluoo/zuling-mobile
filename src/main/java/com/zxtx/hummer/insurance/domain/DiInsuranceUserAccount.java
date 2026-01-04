package com.zxtx.hummer.insurance.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zxtx.hummer.common.domain.AbstractBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 险种类型表
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Getter
@Setter
@TableName("di_insurance_user_account")
public class DiInsuranceUserAccount extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("身份证号")
    private String passWord;

    @ApiModelProperty("1正常2注销3下线")
    private Integer status;

    @ApiModelProperty("名字")
    private String name;

}
