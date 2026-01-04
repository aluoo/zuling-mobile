package com.zxtx.hummer.hk.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.common.vo.IdQueryReq;
import com.zxtx.hummer.exchange.domain.dto.ExchangeOrderCommissionBackDTO;
import com.zxtx.hummer.hk.domain.request.HkApplyOrderQueryReq;
import com.zxtx.hummer.hk.domain.response.HKApplyOrderVO;
import com.zxtx.hummer.hk.service.HkApplyOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
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
@Api(tags = "号卡订单管理")
@RequestMapping("/api/hk/apply/order")
public class HkApplyOrderController extends BaseController {
    @Autowired
    private HkApplyOrderService service;

    @ApiOperation("号卡订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<HKApplyOrderVO>> list(@RequestBody HkApplyOrderQueryReq req) {
        return Response.ok(service.list(req));
    }

    @ApiOperation("导出")
    @Log(value = "号卡订单列表导出", argsPos = {0, 1})
    @RequiresPermissions("hk:apply:order:export")
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportData(@RequestBody HkApplyOrderQueryReq req, HttpServletResponse response) {
        String fileName = ExcelExportUtil.buildName("号卡订单列表导出");
        ExcelExportUtil.setResponseContent(response, fileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            req.setPage(1);
            req.setPageSize(30000);
            List<HKApplyOrderVO> vos = service.list(req).getRows();
            if (CollUtil.isEmpty(vos)) {
                HSSFWorkbook book = new HSSFWorkbook();
                book.createSheet("null");
                book.write(out);
            } else {
                ExcelExportUtil.exportWorkbook(vos).write(out);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
        }
    }

    @ApiOperation("号卡订单详情")
    @RequiresPermissions("hk:apply:order:detail")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Response<HKApplyOrderVO> detail(@RequestBody IdQueryReq req) {
        ValidatorUtil.validateBean(req);
        return Response.ok(service.detail(req.getId()));
    }

    @ApiOperation("号卡订单佣金追回")
    @ResponseBody
    @PostMapping("/commissionBack")
    @RequiresPermissions("hk:apply:order:commission:back")
    @Log(value = "号卡订单佣金追回", argsPos = {0})
    public Response<?> commissionBack(@RequestBody ExchangeOrderCommissionBackDTO req) {
        service.commissionBack(req);
        return Response.ok();
    }
}