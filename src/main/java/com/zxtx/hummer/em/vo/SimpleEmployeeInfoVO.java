package com.zxtx.hummer.em.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/10/8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SimpleEmployeeInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String mobileNumber;
    private String statusStr;
    private String employeeRole;
}