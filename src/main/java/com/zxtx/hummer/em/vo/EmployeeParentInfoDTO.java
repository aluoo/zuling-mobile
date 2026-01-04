package com.zxtx.hummer.em.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author WangWJ
 * @Description
 * @Date 2023/11/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeParentInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String deptName;
    private String name;
    private String mobile;
}