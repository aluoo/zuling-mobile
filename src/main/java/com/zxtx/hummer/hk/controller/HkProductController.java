package com.zxtx.hummer.hk.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.common.vo.IdQueryReq;
import com.zxtx.hummer.hk.domain.request.*;
import com.zxtx.hummer.hk.domain.response.HkProductVO;
import com.zxtx.hummer.hk.domain.response.HkRelationCompanyVO;
import com.zxtx.hummer.hk.service.HkProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "号卡商品管理")
@RequestMapping("/api/hk/product")
public class HkProductController extends BaseController {
    @Autowired
    private HkProductService service;

    @ApiOperation("号卡套餐列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<HkProductVO>> list(@RequestBody HkProductQueryReq req) {
        return Response.ok(service.list(req));
    }

    @ApiOperation("号卡套餐详情")
    @RequiresPermissions("hk:product:detail")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response<HkProductVO> detail(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detail(req.getId()));
    }

    @Log(value = "添加号卡套餐", argsPos = {0})
    @ApiOperation("添加号卡套餐")
    @RequiresPermissions("hk:product:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody HkProductAddReq req) {
        ValidatorUtil.validateBean(req);
        service.add(req);
        return Response.ok();
    }

    @Log(value = "编辑号卡套餐", argsPos = {0})
    @ApiOperation("编辑号卡套餐")
    @RequiresPermissions("hk:product:edit")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody HkProductEditReq req) {
        ValidatorUtil.validateBean(req);
        service.edit(req);
        return Response.ok();
    }

    @Log(value = "切换号卡套餐状态", argsPos = {0})
    @ApiOperation("切换号卡套餐状态")
    @RequiresPermissions("hk:product:switch")
    @RequestMapping(value = "/switch/status", method = RequestMethod.POST)
    public Response<?> switchStatus(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.switchStatus(req.getId());
        return Response.ok();
    }

    @Log(value = "删除号卡套餐", argsPos = {0})
    @ApiOperation("删除号卡套餐")
    @RequiresPermissions("hk:product:remove")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public Response<?> remove(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        service.remove(req.getId());
        return Response.ok();
    }

    @Log(value = "号卡套餐分佣设置", argsPos = {0})
    @ApiOperation("号卡套餐分佣设置")
    @RequiresPermissions("hk:product:commission")
    @RequestMapping(value = "/edit/commission/status", method = RequestMethod.POST)
    public Response<?> editCommissionStatus(@RequestBody HkProductCommissionStatusReq req) {
        ValidatorUtil.validateBean(req);
        service.editCommissionStatus(req);
        return Response.ok();
    }

    @ApiOperation("获取号卡套餐及人员底下门店关联信息列表")
    @RequiresPermissions("hk:product:relation:company:list")
    @RequestMapping(value = "/relation/company/list", method = RequestMethod.POST)
    public Response<PageUtils<HkRelationCompanyVO>> listRelationCompany(@RequestBody HkRelationCompanyReq req) {
        ValidatorUtil.validateBean(req);
        // 套餐及人员底下所有门店信息列表，分页
        return Response.ok(service.listRelationCompany(req));
    }

    @ApiOperation("获取号卡套餐所有已关联门店")
    @RequiresPermissions("hk:product:relation:company:list")
    @RequestMapping(value = "/relation/company/info", method = RequestMethod.POST)
    public Response<List<HkRelationCompanyVO>> listRelationCompanyInfo(@RequestBody HkRelationCompanyReq req) {
        ValidatorUtil.validateBean(req);
        // 套餐所有已关联的门店列表，不分页
        return Response.ok(service.listRelationCompanyInfo(req));
    }

    @Log(value = "号卡套餐关联门店编辑", argsPos = {0})
    @ApiOperation("号卡套餐关联门店编辑")
    @RequiresPermissions("hk:product:relation:company:edit")
    @RequestMapping(value = "/relation/company/edit", method = RequestMethod.POST)
    public Response<?> editRelationCompany(@RequestBody HkRelationCompanyEditReq req) {
        ValidatorUtil.validateBean(req);
        service.editRelationCompany(req);
        return Response.ok();
    }
}