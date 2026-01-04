package com.zxtx.hummer.system.req;

import lombok.Data;

/**
 * @author chenjian
 * @create 2023/5/22 13:37
 * @desc
 **/
@Data
public class ContentQueryReq {

    private int page = 1;

    private int pageSize = 10;
}
