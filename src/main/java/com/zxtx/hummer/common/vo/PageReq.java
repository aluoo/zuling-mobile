package com.zxtx.hummer.common.vo;

import lombok.Data;

@Data
public class PageReq {
    private int page = 1;

    private int pageSize = 10;
}
