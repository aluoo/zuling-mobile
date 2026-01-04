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
public class CompanyCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ExcelField(name = "门店名称")
    @NotBlank(message = "门店名称不能为空")
    private String name;

    @ExcelField(name = "负责人")
    @NotBlank(message = "负责人不能为空")
    private String contact;

    @ExcelField(name = "负责人手机号")
    @NotBlank(message = "负责人手机号不能为空")
    private String contactMobile;

    @ExcelField(name = "地址")
    @NotBlank(message = "地址不能为空")
    private String address;

    @ExcelField(name = "邀请代理手机号")
    @NotBlank(message = "邀请代理手机号不能为空")
    private String aplMobile;

    // 2门店3连锁店4服务商
    @ExcelField(name = "类型")
    @NotBlank(message = "类型不能为空")
    private String typeStr;

    private Integer type;

    @ExcelField(name = "拉新模式")
    @NotBlank(message = "拉新模式不能为空")
    // 3换机模式4一键更新
    private String exchangeTypeStr;

    private Integer exchangeType;

}