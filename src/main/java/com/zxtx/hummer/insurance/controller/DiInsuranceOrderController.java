package com.zxtx.hummer.insurance.controller;

import cn.hutool.core.collection.CollUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.insurance.domain.DiInsuranceOrder;
import com.zxtx.hummer.insurance.enums.DiOrderStatusEnum;
import com.zxtx.hummer.insurance.enums.DiOrderSubStatusEnum;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderAuditReq;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderEditReq;
import com.zxtx.hummer.insurance.req.DiInsuranceOrderReq;
import com.zxtx.hummer.insurance.service.DiInsuranceOrderManageService;
import com.zxtx.hummer.insurance.service.DiInsuranceOrderService;
import com.zxtx.hummer.insurance.vo.DiInsuranceOrderVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceOrderViewVO;
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
 * 数保订单表 前端控制器
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Slf4j
@Api(tags = "碎屏险-订单管理")
@RestController
@RequestMapping("/api/di/insurance/order")
public class DiInsuranceOrderController {
    @Autowired
    DiInsuranceOrderManageService insuranceOrderManageService;
    @Autowired
    private DiInsuranceOrderService insuranceOrderService;

    @ApiOperation("订单列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<DiInsuranceOrderVO>> list(@RequestBody DiInsuranceOrderReq req) {
        return Response.ok(PageUtils.create(insuranceOrderManageService.selectPage(req)));
    }

    @Log(value = "数保订单列表导出", argsPos = {0, 1})
    @ApiOperation("订单列表-导出")
    @RequiresPermissions("di:order:export")
    @RequestMapping(value = "/list/export", method = RequestMethod.POST)
    public void listExport(@RequestBody DiInsuranceOrderReq req, HttpServletResponse response) {
        String filename = ExcelExportUtil.buildName("导出数保订单列表");
        ExcelExportUtil.setResponseContent(response, filename);
        try (OutputStream out = response.getOutputStream()) {
            req.setPage(1);
            req.setPageSize(30000);
            List<DiInsuranceOrderVO> vos = insuranceOrderManageService.selectPage(req);
            if (CollUtil.isEmpty(vos)) {
                HSSFWorkbook book = new HSSFWorkbook();
                book.createSheet("null");
                book.write(out);
            } else {
                vos.forEach(DiInsuranceOrderVO::setPriceStr);
                ExcelExportUtil.exportWorkbook(vos).write(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("订单详情")
    @ResponseBody
    @PostMapping("/detail/{id}")
    public Response<DiInsuranceOrderViewVO> detail(@PathVariable Long id) {
        return Response.ok(insuranceOrderManageService.detail(id));
    }

    @Log(value = "数保订单资料审核", argsPos = {0})
    @ApiOperation("数保订单资料审核")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public Response<?> auditOrder(@RequestBody DiInsuranceOrderAuditReq req) {
        ValidatorUtil.validateBean(req);
        insuranceOrderManageService.auditOrder(req);
        return Response.ok();
    }

    @Log(value = "数保订单退款审核", argsPos = {0})
    @ApiOperation("数保订单退款审核")
    @RequestMapping(value = "/refund/audit", method = RequestMethod.POST)
    public Response<?> auditRefund(@RequestBody DiInsuranceOrderAuditReq req) {
        ValidatorUtil.validateBean(req);
        insuranceOrderManageService.auditRefund(req);
        return Response.ok();
    }

    @Log(value = "保单上传", argsPos = {0})
    @ApiOperation("保单上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Response<?> uploadInsurance(@RequestBody DiInsuranceOrderAuditReq req) {
        ValidatorUtil.validateBean(req);
        insuranceOrderManageService.uploadInsurance(req);
        return Response.ok();
    }

    @Deprecated
    @Log(value = "修改保单资料", argsPos = {0})
    @ApiOperation("修改保单资料 (暂时不需要)")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Response<?> editOrder(@RequestBody DiInsuranceOrderEditReq req) {
        ValidatorUtil.validateBean(req);
        insuranceOrderManageService.editOrder(req);
        return Response.ok();
    }

    @ApiOperation("数保订单手动退款")
    @RequestMapping(value = "/refund/manual", method = RequestMethod.POST)
    public Response<?> manualRefund(@RequestBody DiInsuranceOrderAuditReq req) {
        insuranceOrderManageService.manualRefund(req);
        return Response.ok();
    }

    @ApiOperation("数保订单待审核-提醒")
    @RequestMapping(value = "/checkVoice", method = RequestMethod.GET)
    public Response<Boolean> checkVoice() {
        Long count = insuranceOrderService.lambdaQuery()
                .in(DiInsuranceOrder::getStatus, DiOrderStatusEnum.PENDING_AUDIT.getCode(), DiOrderStatusEnum.REFUNDING.getCode())
                .in(DiInsuranceOrder::getSubStatus, DiOrderSubStatusEnum.PENDING_AUDIT_ORDER.getCode(), DiOrderSubStatusEnum.PENDING_AUDIT_REFUND.getCode())
                .count();
        return Response.ok(count > 0);
    }
}