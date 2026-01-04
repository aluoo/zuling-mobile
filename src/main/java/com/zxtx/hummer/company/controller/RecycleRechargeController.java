package com.zxtx.hummer.company.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.company.req.RechargeCheckReq;
import com.zxtx.hummer.company.req.RecycleRechargeReq;
import com.zxtx.hummer.company.service.RecycleRechargeLogService;
import com.zxtx.hummer.company.vo.RecycleRechargeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "回收商充值相关")
@RequestMapping("api/recycle/recharge")
public class RecycleRechargeController {
    @Autowired
    private RecycleRechargeLogService recycleRechargeLogService;


    @ApiOperation("列表")
    @ResponseBody
    @GetMapping("/list")
    public Response<PageUtils<RecycleRechargeVo>> list(RecycleRechargeReq req) {
        List<RecycleRechargeVo> resultList = recycleRechargeLogService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @Log(value = "审核", argsPos = {0})
    @ApiOperation("审核")
    @ResponseBody
    @PostMapping("/check")
    public Response check(@Valid @RequestBody RechargeCheckReq req) {
        recycleRechargeLogService.check(req);
        return Response.ok();
    }


}
