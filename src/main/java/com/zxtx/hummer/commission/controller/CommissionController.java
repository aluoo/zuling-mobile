package com.zxtx.hummer.commission.controller;

import cn.hutool.core.bean.BeanUtil;
import com.zxtx.hummer.commission.domain.CommissionType;
import com.zxtx.hummer.commission.domain.CommissionTypePackage;
import com.zxtx.hummer.commission.req.CommissionQueryReq;
import com.zxtx.hummer.commission.req.CommissionTypePackageReq;
import com.zxtx.hummer.commission.rsp.CommissionTypePackageVO;
import com.zxtx.hummer.commission.service.CommissionTypePackageService;
import com.zxtx.hummer.commission.service.CommissionTypeService;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Slf4j
@RestController
@Api(tags = "分佣管理")
@RequestMapping("api/commission")
public class CommissionController {

    @Autowired
    private CommissionTypeService commissionTypeService;
    @Autowired
    private CommissionTypePackageService commissionTypePackageService;

    @ApiOperation("方案列表")
    @ResponseBody
    @PostMapping("/type/list")
    public Response<PageUtils<CommissionType>> typeList(@RequestBody CommissionQueryReq req) {
        return Response.ok(commissionTypeService.list(req));
    }

    @ApiOperation("增加佣金方案")
    @ResponseBody
    @Log(value = "增加佣金方案", argsPos = {0})
    @PostMapping("/type/add")
    public Response add(@RequestBody CommissionType req) {
        req.setDeleted(false);
        commissionTypeService.save(req);
        return Response.ok();
    }

    @ApiOperation("方案套餐列表")
    @ResponseBody
    @PostMapping("/package/list")
    public Response<PageUtils<CommissionTypePackageVO>> packageList(@RequestBody CommissionQueryReq req) {
        return Response.ok(commissionTypePackageService.list(req));
    }

    @ApiOperation("增加佣金方案套餐")
    @Log(value = "增加佣金方案套餐", argsPos = {0})
    @ResponseBody
    @PostMapping("/package/add")
    public Response packageAdd(@RequestBody CommissionTypePackageReq req) {
        commissionTypePackageService.savePackage(req);
        return Response.ok();
    }

    @ApiOperation("修改佣金方案套餐")
    @Log(value = "修改佣金方案套餐", argsPos = {0})
    @ResponseBody
    @PostMapping("/package/edit")
    public Response packageEdit(@RequestBody CommissionTypePackageReq req) {
        CommissionTypePackage commissionTypePackage = new CommissionTypePackage();
        BeanUtil.copyProperties(req, commissionTypePackage);
        commissionTypePackage.setDeleted(false);
        commissionTypePackage.setMaxCommissionFee(req.getMaxCommissionFee().multiply(new BigDecimal(100)).longValue());
        commissionTypePackageService.updateById(commissionTypePackage);
        return Response.ok();
    }






}