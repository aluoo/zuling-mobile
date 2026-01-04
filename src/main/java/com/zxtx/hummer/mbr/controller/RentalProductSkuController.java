package com.zxtx.hummer.mbr.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.product.domain.request.*;
import com.zxtx.hummer.product.domain.response.ProductSkuVO;
import com.zxtx.hummer.product.domain.validator.ProductValidatorGroup;
import com.zxtx.hummer.product.service.ProductManageService;
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
 * @Date 2025/4/24
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "租机手机sku管理")
@RequestMapping("/api/product/rental")
public class RentalProductSkuController extends BaseController {
    @Autowired
    private ProductManageService service;

    @ApiOperation("租机SKU列表")
    @RequestMapping(value = "/sku/list", method = RequestMethod.POST)
    public Response<PageUtils<ProductSkuVO>> listRentalSku(@RequestBody ProductQueryReq req) {
        return Response.ok(service.listRentalSku(req));
    }

    @Log(value = "添加租机SKU", argsPos = {0})
    @RequiresPermissions("product:rental:sku:add")
    @ApiOperation("添加租机SKU")
    @RequestMapping(value = "/sku/add", method = RequestMethod.POST)
    public Response<?> addRentalSku(@RequestBody ProductSkuAddReq req) {
        ValidatorUtil.validateBean(req);
        service.addRentalSku(req);
        return Response.ok(null);
    }

    @Log(value = "批量添加租机SKU", argsPos = {0})
    @RequiresPermissions("product:rental:sku:add:batch")
    @ApiOperation("批量添加租机SKU")
    @RequestMapping(value = "/sku/add/batch", method = RequestMethod.POST)
    public Response<?> addRentalSkuBatch(@RequestBody ProductSkuAddBatchReq req) {
        ValidatorUtil.validateBean(req);
        service.addRentalSkuBatch(req);
        return Response.ok(null);
    }

    @Log(value = "编辑租机SKU", argsPos = {0})
    @RequiresPermissions("product:rental:sku:edit")
    @ApiOperation("编辑租机SKU")
    @RequestMapping(value = "/sku/edit", method = RequestMethod.POST)
    public Response<?> editRentalSku(@RequestBody ProductSkuOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Edit.class);
        service.editRentalSku(req);
        return Response.ok(null);
    }

    @Log(value = "删除租机SKU", argsPos = {0})
    @RequiresPermissions("product:rental:sku:del")
    @ApiOperation("删除租机SKU")
    @RequestMapping(value = "/sku/delete", method = RequestMethod.POST)
    public Response<?> deleteRentalSku(@RequestBody ProductOperatorReq req) {
        ValidatorUtil.validateBean(req, ProductValidatorGroup.Delete.class);
        service.deleteRentalSku(req.getId());
        return Response.ok();
    }
}