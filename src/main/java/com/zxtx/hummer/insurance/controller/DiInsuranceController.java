package com.zxtx.hummer.insurance.controller;

import cn.hutool.core.collection.CollUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.insurance.req.*;
import com.zxtx.hummer.insurance.service.DiInsuranceService;
import com.zxtx.hummer.insurance.service.DiProductManageService;
import com.zxtx.hummer.insurance.vo.DiInsuranceSkuVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * <p>
 * 保险产品表 前端控制器
 * </p>
 *
 * @author aycx
 * @since 2024-05-21
 */
@Slf4j
@Api(tags = "碎屏险-产品管理")
@RestController
@RequestMapping("/api/di/insurance")
public class DiInsuranceController {

    @Autowired
    private DiInsuranceService diInsuranceService;
    @Autowired
    private DiProductManageService productManageService;

    @ApiOperation("产品列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<DiInsuranceVO>> list(@RequestBody DiInsuranceReq req) {
        return Response.ok(PageUtils.create(diInsuranceService.selectPage(req)));
    }

    @Log(value = "数保产品列表导出", argsPos = {0, 1})
    @ApiOperation("产品列表-导出")
    @RequiresPermissions("di:insurance:export")
    @RequestMapping(value = "/list/export", method = RequestMethod.POST)
    public void listExport(@RequestBody DiInsuranceReq req, HttpServletResponse response) {
        String filename = ExcelExportUtil.buildName("导出数保产品列表");
        ExcelExportUtil.setResponseContent(response, filename);
        try (OutputStream out = response.getOutputStream()) {
            req.setPage(1);
            req.setPageSize(30000);
            List<DiInsuranceVO> vos = diInsuranceService.selectPage(req);
            if (CollUtil.isEmpty(vos)) {
                HSSFWorkbook book = new HSSFWorkbook();
                book.createSheet("null");
                book.write(out);
            } else {
                ExcelExportUtil.exportWorkbook(vos).write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Log(value = "添加商品", argsPos = {0})
    @ApiOperation("添加商品")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Response add(@RequestBody DiProductOperatorDTO req) {
        ValidatorUtil.validateBean(req);
        diInsuranceService.add(req);
        return Response.ok();
    }

    @ApiOperation("根据ID获取商品详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Response<DiInsuranceViewVO> detailById(@PathVariable("id") Long productId) {
        return Response.ok(diInsuranceService.detailById(productId));
    }

    @Log(value = "编辑商品", argsPos = {0})
    @ApiOperation("编辑商品")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response edit(@RequestBody DiProductOperatorDTO req) {
        ValidatorUtil.validateBean(req);
        diInsuranceService.edit(req);
        return Response.ok();
    }

    @Log(value = "上/下架商品", argsPos = {0})
    @ApiOperation("上/下架商品")
    @RequestMapping(value = "/switch/status", method = RequestMethod.POST)
    public Response switchProductActivatedStatus(@RequestBody DiUpdateStatusDTO req) {
        diInsuranceService.switchProductActivatedStatus(req);
        return Response.ok();
    }

    @Log(value = "价格区间设置", argsPos = {0})
    @ApiOperation("价格区间设置")
    @RequestMapping(value = "/priceSave", method = RequestMethod.POST)
    public Response priceSave(@RequestBody DiInsurancePriceDTO req) {
        diInsuranceService.priceSave(req);
        return Response.ok();
    }

    @Log(value = "SKU产品底价设置", argsPos = {0})
    @ApiOperation("SKU产品底价设置")
    @RequestMapping(value = "/sku", method = RequestMethod.POST)
    public Response skuSave(@RequestBody DiInsuranceSkuDTO req) {
        productManageService.skuSave(req);
        return Response.ok();
    }

    @Log(value = "SKU产品底价设置详情", argsPos = {0})
    @ApiOperation("SKU产品底价设置详情")
    @GetMapping(value = "/skuDetail/{skuId}")
    public Response<List<DiInsuranceSkuVO>> skuDetail(@PathVariable Long skuId) {
        return Response.ok( productManageService.skuDetail(skuId));
    }

}