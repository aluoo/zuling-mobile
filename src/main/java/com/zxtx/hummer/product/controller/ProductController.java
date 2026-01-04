package com.zxtx.hummer.product.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.product.domain.request.*;
import com.zxtx.hummer.product.domain.response.ProductSkuVO;
import com.zxtx.hummer.product.domain.response.ProductVO;
import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
import com.zxtx.hummer.product.service.ProductManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/1/25
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "商品管理")
@RequestMapping("/api/product")
public class ProductController extends BaseController {
    @Autowired
    private ProductManageService service;

    @ApiOperation("商品列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<ProductVO>> listProduct(@RequestBody ProductQueryReq req) {
        //return service.listProduct(req);
        return Response.ok(service.listProduct(req));
    }

    @ApiOperation("根据ID获取商品详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Response<ProductVO> detailById(@PathVariable("id") Long productId) {
        return Response.ok(service.detailById(productId));
    }

    @Log(value = "添加商品", argsPos = {0})
    @ApiOperation("添加商品")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req);
        service.add(req);
        return Response.ok();
    }

    @Log(value = "编辑商品", argsPos = {0})
    @ApiOperation("编辑商品")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Edit.class);
        service.edit(req);
        return Response.ok(null);
    }

    @Log(value = "删除商品", argsPos = {0})
    @ApiOperation("删除商品")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Response<?> delete(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Delete.class);
        service.delete(req.getId());
        return Response.ok();
    }

    @Log(value = "上/下架商品", argsPos = {0})
    @ApiOperation("上/下架商品")
    @RequestMapping(value = "/switch/activated_status", method = RequestMethod.POST)
    public Response<?> switchProductActivatedStatus(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Switch.class);
        service.switchProductActivatedStatus(req);
        return Response.ok();
    }

    @Log(value = "数保售卖开关", argsPos = {0})
    @ApiOperation("数保售卖开关")
    @RequestMapping(value = "/switch/insurance_status", method = RequestMethod.POST)
    public Response<?> switchProductInsuranceStatus(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Switch.class);
        service.switchProductInsuranceStatus(req);
        return Response.ok();
    }

    @Log(value = "是否参与租机开关", argsPos = {0})
    @ApiOperation("是否参与租机开关")
    @RequestMapping(value = "/switch/rental_status", method = RequestMethod.POST)
    public Response<?> switchProductRentalStatus(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Switch.class);
        service.switchProductRentalStatus(req);
        return Response.ok();
    }

    @ApiOperation("手机SKU列表")
    @RequestMapping(value = "/sku/list", method = RequestMethod.POST)
    public Response<PageUtils<ProductSkuVO>> listSku(@RequestBody ProductQueryReq req) {
        return Response.ok(service.listSku(req));
    }

    @Log(value = "添加SKU", argsPos = {0})
    @RequiresPermissions("product:sku:add")
    @ApiOperation("添加SKU")
    @RequestMapping(value = "/sku/add", method = RequestMethod.POST)
    public Response<?> addSku(@RequestBody ProductSkuAddReq req) {
        ValidatorUtil.validateBean(req);
        service.addSku(req);
        return Response.ok(null);
    }

    @Log(value = "批量添加SKU", argsPos = {0})
    @RequiresPermissions("product:sku:add:batch")
    @ApiOperation("批量添加SKU")
    @RequestMapping(value = "/sku/add/batch", method = RequestMethod.POST)
    public Response<?> addSkuBatch(@RequestBody ProductSkuAddBatchReq req) {
        ValidatorUtil.validateBean(req);
        service.addSkuBatch(req);
        return Response.ok(null);
    }

    @Log(value = "编辑SKU", argsPos = {0})
    @ApiOperation("编辑SKU")
    @RequestMapping(value = "/sku/edit", method = RequestMethod.POST)
    public Response<?> editSku(@RequestBody ProductSkuOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Edit.class);
        service.editSku(req);
        return Response.ok(null);
    }

    @Log(value = "删除SKU", argsPos = {0})
    @ApiOperation("删除SKU")
    @RequestMapping(value = "/sku/delete", method = RequestMethod.POST)
    public Response<?> deleteSku(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Delete.class);
        service.deleteSku(req.getId());
        return Response.ok();
    }
}