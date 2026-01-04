package com.zxtx.hummer.commission.dto;

import lombok.Data;

import java.util.List;

@Data
public class MemberDTO {

    private int memberNum;
    private int deptNum;
    private List<EmployeeDTO> members;
}
