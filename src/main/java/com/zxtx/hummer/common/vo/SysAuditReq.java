package com.zxtx.hummer.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SysAuditReq {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;

    private Long userId;

    private String username;

    private String operation;

    private String oldValue;

    private String newValue;

}
