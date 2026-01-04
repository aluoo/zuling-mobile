package com.zxtx.hummer.commission.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckSumVO;
import com.zxtx.hummer.commission.dto.CommissionSettleCheckVO;
import com.zxtx.hummer.commission.dto.CommissionSettleDTO;
import com.zxtx.hummer.commission.enums.CommissionPackage;
import com.zxtx.hummer.commission.req.CommissionSettleCheckReq;
import com.zxtx.hummer.commission.req.SettleLogQueryReq;
import com.zxtx.hummer.commission.service.CommissionSettleCheckService;
import com.zxtx.hummer.commission.service.CommissionSettleService;
import com.zxtx.hummer.commission.service.CommissionTypePackageService;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.vo.ItemCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(tags = "结算记录")
@RequestMapping("api/commission/settle")
public class CommissionSettleController {

    @Autowired
    private CommissionSettleService commissionSettleService;
    @Autowired
    private CommissionSettleCheckService commissionSettleCheckService;
    @Autowired
    private CommissionTypePackageService commissionTypePackageService;

    @Log(value = "系统结算记录", argsPos = {0, 1})
    @ApiOperation("系统结算记录")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<List<CommissionSettleDTO>>> list(@RequestBody SettleLogQueryReq req) {
        List<CommissionSettleDTO> accountLogs = commissionSettleService.pageList(req);
        return Response.ok(PageUtils.create(accountLogs));
    }


    @ApiOperation("合伙人账单业务类型")
    @ResponseBody
    @PostMapping("/bdTypeList")
    public Response typeList() {
        List<ItemCode> listMap = new ArrayList<>();
        commissionTypePackageService.list().forEach(item -> {
            listMap.add(ItemCode.builder()
                    .code(item.getId().toString())
                    .name(item.getName()).build());
        });
        return Response.ok(listMap);
    }

    @ApiOperation("合伙人账单")
    @ResponseBody
    @PostMapping("/bdList")
    public Response<PageUtils<CommissionSettleCheckVO>> list(@RequestBody CommissionSettleCheckReq req) {
        return Response.ok(PageUtils.create(commissionSettleCheckService.listOrder(req)));
    }

    @ApiOperation("合伙人统计")
    @ResponseBody
    @PostMapping("/bdSum")
    public Response<CommissionSettleCheckSumVO> bdSum(@RequestBody CommissionSettleCheckReq req) {
        return Response.ok(commissionSettleCheckService.SumByParam(req));
    }

    @ApiOperation("导出合伙人账单")
    @Log(value = "导出合伙人账单", argsPos = {0, 1})
    @PostMapping(value = "/export")
    public void exportWithDraw(@RequestBody CommissionSettleCheckReq req, HttpServletResponse response) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "合伙人账单" + format.format(new Date().getTime()) + ".xls";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "iso-8859-1"));
        OutputStream out = response.getOutputStream();
        try {
            req.setPage(1);
            req.setPageSize(20000);
            List<CommissionSettleCheckVO> resultList = commissionSettleCheckService.listOrder(req);
            ExcelExportUtil.exportWorkbook(resultList).write(out);
        } finally {
            out.close();
        }
    }

    @ApiOperation("导出结算记录")
    @Log(value = "导出结算记录", argsPos = {0, 1})
    @RequiresPermissions("commission:settle:export")
    @PostMapping(value = "/export/settle")
    public void exportSettleList(@RequestBody SettleLogQueryReq req, HttpServletResponse response) throws Exception {
        String fileName = ExcelExportUtil.buildName("导出结算记录");
        ExcelExportUtil.setResponseContent(response, fileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            req.setPage(1);
            req.setPageSize(20000);
            List<CommissionSettleDTO> vos = commissionSettleService.pageList(req);
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