package com.zxtx.hummer.insurance.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.insurance.domain.dto.DiInsuranceFixOrderSettlementDTO;
import com.zxtx.hummer.insurance.req.DiInsuranceFixOrderSettlementQueryReq;
import com.zxtx.hummer.insurance.service.FixOrderSettlementManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/10/17
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "数保理赔打款管理")
@RequestMapping("/api/di/insurance/fixOrder/settlement")
public class InsuranceFixOrderSettlementController extends BaseController {
    @Autowired
    FixOrderSettlementManageService service;

    @ApiOperation("列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<DiInsuranceFixOrderSettlementDTO>> list(@RequestBody DiInsuranceFixOrderSettlementQueryReq req) {
        return Response.ok(service.list(req));
    }

    @ApiOperation("详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Response<DiInsuranceFixOrderSettlementDTO> detail(@PathVariable("id") Long id) {
        return Response.ok(service.detail(id));
    }

    @ApiOperation("导出")
    @Log(value = "数保理赔打款导出", argsPos = {0, 1})
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    public void exportData(@RequestBody DiInsuranceFixOrderSettlementQueryReq req, HttpServletResponse response) {
        String fileName = ExcelExportUtil.buildName("数保打款列表导出");
        ExcelExportUtil.setResponseContent(response, fileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            req.setPage(1);
            req.setPageSize(30000);
            List<DiInsuranceFixOrderSettlementDTO> vos = service.list(req).getRows();
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

    @ApiOperation("导入")
    @Log(value = "数保理赔打款导入", noArgs = true)
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public Response<?> importData(@RequestParam("file") MultipartFile file) {
        return Response.ok(service.importData(file));
    }
}