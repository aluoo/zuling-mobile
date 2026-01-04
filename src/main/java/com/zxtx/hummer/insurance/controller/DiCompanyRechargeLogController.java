package com.zxtx.hummer.insurance.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.company.req.RechargeCheckReq;
import com.zxtx.hummer.company.req.RecycleRechargeReq;
import com.zxtx.hummer.company.vo.RecycleRechargeVo;
import com.zxtx.hummer.insurance.service.DiCompanyRechargeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 数保门店账户充值记录表 前端控制器
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Slf4j
@RestController
@Api(tags = "碎屏险-门店充值管理")
@RequestMapping("/api/di/company/recharge")
public class DiCompanyRechargeLogController {
    @Autowired
    DiCompanyRechargeLogService diCompanyRechargeLogService;


    @ApiOperation("列表")
    @ResponseBody
    @GetMapping("/list")
    public Response<PageUtils<RecycleRechargeVo>> list(RecycleRechargeReq req) {
        List<RecycleRechargeVo> resultList = diCompanyRechargeLogService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @Log(value = "审核", argsPos = {0})
    @ApiOperation("审核")
    @ResponseBody
    @PostMapping("/check")
    public Response check(@Valid @RequestBody RechargeCheckReq req) {
        diCompanyRechargeLogService.check(req);
        return Response.ok();
    }
}
