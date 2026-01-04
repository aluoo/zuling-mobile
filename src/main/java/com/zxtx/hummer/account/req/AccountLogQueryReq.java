package com.zxtx.hummer.account.req;

import lombok.Data;

@Data
public class AccountLogQueryReq {

    private Long employeeId;
//    private String employeeId;

    private int page = 1;

    private int pageSize = 10;
}
