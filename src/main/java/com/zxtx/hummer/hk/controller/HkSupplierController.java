package com.zxtx.hummer.hk.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.common.vo.IdQueryReq;
import com.zxtx.hummer.hk.domain.request.HkSupplierAddReq;
import com.zxtx.hummer.hk.domain.request.HkSupplierEditReq;
import com.zxtx.hummer.hk.domain.request.HkSupplierQueryReq;
import com.zxtx.hummer.hk.domain.response.HkSupplierVO;
import com.zxtx.hummer.hk.service.HkSupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/4
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "号卡供应商管理")
@RequestMapping("/api/hk/supplier")
public class HkSupplierController extends BaseController {
    @Autowired
    private HkSupplierService service;

    @ApiOperation("供应商列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<HkSupplierVO>> list(@RequestBody HkSupplierQueryReq req) {
        return Response.ok(service.list(req));
    }

    @ApiOperation("供应商详情")
    @RequiresPermissions("hk:supplier:detail")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response<HkSupplierVO> detail(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detail(req.getId()));
    }

    @Log(value = "添加供应商", argsPos = {0})
    @ApiOperation("添加供应商")
    @RequiresPermissions("hk:supplier:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody HkSupplierAddReq req) {
        ValidatorUtil.validateBean(req);
        service.add(req);
        return Response.ok();
    }

    @Log(value = "编辑供应商", argsPos = {0})
    @ApiOperation("编辑供应商")
    @RequiresPermissions("hk:supplier:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody HkSupplierEditReq req) {
        ValidatorUtil.validateBean(req);
        service.edit(req);
        return Response.ok();
    }

    @Log(value = "切换供应商状态", argsPos = {0})
    @ApiOperation("切换供应商状态")
    @RequiresPermissions("hk:supplier:switch")
    @RequestMapping(value = "/switch/status", method = RequestMethod.POST)
    public Response<?> switchStatus(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.switchStatus(req.getId());
        return Response.ok();
    }

    @Log(value = "删除供应商套餐", argsPos = {0})
    @ApiOperation("删除供应商套餐")
    @RequiresPermissions("hk:supplier:remove")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public Response<?> remove(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.remove(req.getId());
        return Response.ok();
    }
}