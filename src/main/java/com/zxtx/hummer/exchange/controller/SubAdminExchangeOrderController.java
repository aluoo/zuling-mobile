package com.zxtx.hummer.exchange.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckSumVO;
import com.zxtx.hummer.commission.dto.CommissionSettleDTO;
import com.zxtx.hummer.commission.req.SettleLogQueryReq;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.company.domain.Company;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.exchange.req.ExchangeOrderReq;
import com.zxtx.hummer.exchange.service.MbExchangeOrderService;
import com.zxtx.hummer.exchange.vo.ExchangeOrderSummaryVO;
import com.zxtx.hummer.exchange.vo.ExchangeOrderViewVO;
import com.zxtx.hummer.exchange.vo.ExchangeSubAdminOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/12
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Api(tags = "子后台-晒单管理")
@RestController
@RequestMapping("api/sub")
public class SubAdminExchangeOrderController {
    @Autowired
    private EmService emService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private MbExchangeOrderService mbExchangeOrderService;
    @Autowired
    private CommissionSettleService commissionSettleService;

    @ApiOperation("区域经理列表")
    @RequestMapping(value = "/info/query/areaList/by_bd", method = RequestMethod.GET)
    public Response<List<Employee>> areaList() {
        if (ShiroUtils.getUser().getEmployeeId() == null) {
            return Response.ok(new ArrayList<>());
        }
        return Response.ok(emService.areaList(ShiroUtils.getUser().getEmployeeId().toString()));
    }

    @ApiOperation("代理列表")
    @RequestMapping(value = "/info/query/agentList/{employeeId}", method = RequestMethod.GET)
    public Response<List<Employee>> agentList(@PathVariable("employeeId") String employeeId) {
        if (StrUtil.isBlank(employeeId)) {
            return Response.ok(new ArrayList<>());
        }
        return Response.ok(emService.agentList(employeeId));
    }

    @ApiOperation("连锁店列表")
    @RequestMapping(value = "/info/query/chainCompanyList/{employeeId}", method = RequestMethod.GET)
    public Response<List<Company>> chainCompanyList(@PathVariable("employeeId") Long employeeId) {
        if (employeeId == null) {
            return Response.ok(new ArrayList<>());
        }
        return Response.ok(companyService.getChainCompanyByBdEmployeeId(employeeId));
    }

    @ApiOperation("系统结算记录")
    @ResponseBody
    @PostMapping("/commission/settle/list")
    public Response<PageUtils<List<CommissionSettleDTO>>> commissionSettleList(@RequestBody SettleLogQueryReq req) {
        List<CommissionSettleDTO> accountLogs = commissionSettleService.subAdminCommissionSettleList(req);
        return Response.ok(PageUtils.create(accountLogs));
    }

    @ApiOperation("系统结算记录计统计")
    @ResponseBody
    @PostMapping("/commission/settle/summary")
    public Response<CommissionSettleCheckSumVO> commissionSettleSummary(@RequestBody SettleLogQueryReq req) {
        return Response.ok(commissionSettleService.sumByParam(req));
    }

    @ApiOperation("晒单列表")
    @RequestMapping(value = "/exchange/order/list", method = RequestMethod.POST)
    public Response<PageUtils<ExchangeSubAdminOrderVO>> exchangeOrderList(@RequestBody ExchangeOrderReq req) {
        return Response.ok(mbExchangeOrderService.subAdminOrderList(req));
    }

    @ApiOperation("晒单详情")
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public Response<ExchangeOrderViewVO> exchangeOrderView(@PathVariable("id") Long id) {
        return Response.ok(mbExchangeOrderService.subAdminOrderView(id));
    }

    @ApiOperation("晒单总计")
    @RequestMapping(value = "/exchange/order/summary", method = RequestMethod.POST)
    public Response<ExchangeOrderSummaryVO> exchangeOrderSummary(@RequestBody ExchangeOrderReq req) {
        return Response.ok(mbExchangeOrderService.subAdminOrderSummary(req));
    }

    @ApiOperation("子后台晒单列表-导出")
    @Log(value = "子后台晒单列表-导出", argsPos = {0, 1})
    @RequiresPermissions("sub:exchange:order:export")
    @RequestMapping(value = "/exchange/order/list/export", method = RequestMethod.POST)
    public void exchangeOrderListExport(@RequestBody ExchangeOrderReq req, HttpServletResponse response) {
        String fileName = ExcelExportUtil.buildName("晒单列表导出");
        ExcelExportUtil.setResponseContent(response, fileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            req.setPage(1);
            req.setPageSize(30000);
            List<ExchangeSubAdminOrderVO> vos = mbExchangeOrderService.subAdminOrderList(req).getRows();
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

    @ApiOperation("子后台结算记录-导出")
    @Log(value = "子后台结算记录-导出", argsPos = {0, 1})
    @RequiresPermissions("sub:commission:settle:export")
    @PostMapping(value = "/commission/settle/list/export")
    public void exportSettleList(@RequestBody SettleLogQueryReq req, HttpServletResponse response) {
        String fileName = ExcelExportUtil.buildName("导出结算记录");
        ExcelExportUtil.setResponseContent(response, fileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            req.setPage(1);
            req.setPageSize(20000);
            List<CommissionSettleDTO> vos = commissionSettleService.subAdminCommissionSettleList(req);
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
}