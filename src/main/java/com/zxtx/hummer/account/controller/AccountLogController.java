package com.zxtx.hummer.account.controller;

import com.zxtx.hummer.account.dto.EmployeeAccountLogDTO;
import com.zxtx.hummer.account.req.AccountLogQueryReq;
import com.zxtx.hummer.account.service.IEmployeeAccountLogService;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.PageUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "资金明细")
@RequestMapping("/api/employee/account")
public class AccountLogController {

    @Autowired
    private IEmployeeAccountLogService employeeAccountLogService;

    @Log(value = "员工账户余额变动明细", argsPos = {0, 1})
    @ResponseBody
    @PostMapping("/accountLog/list")
    public Response<PageUtils<EmployeeAccountLogDTO>> list(@RequestBody AccountLogQueryReq req) {
        List<EmployeeAccountLogDTO> accountLogs = employeeAccountLogService.pageList(req.getEmployeeId(), req.getPage(), req.getPageSize());
        return Response.ok(PageUtils.create(accountLogs));
    }


}
