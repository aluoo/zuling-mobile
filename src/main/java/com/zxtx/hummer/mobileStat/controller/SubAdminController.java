package com.zxtx.hummer.mobileStat.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.mobileStat.dto.IndexStatisticsReq;
import com.zxtx.hummer.mobileStat.dto.IndexStatisticsVO;
import com.zxtx.hummer.mobileStat.service.SubAdminStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/8/9
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Api(tags = "子后台")
@RestController
@RequestMapping("api/sub/")
public class SubAdminController {
    @Autowired
    private SubAdminStatisticsService service;

    @ApiOperation("子后台首页统计")
    @RequestMapping(value = "/index/stat", method = RequestMethod.POST)
    public Response<IndexStatisticsVO> indexStatistics(@RequestBody IndexStatisticsReq req) {
        return Response.ok(service.indexStatistics(req));
    }

    @ApiOperation("子后台首页统计-导出")
    @Log(value = "子后台首页统计-导出", argsPos = {0, 1})
    @RequiresPermissions("sub:index:stat:export")
    @RequestMapping(value = "/index/stat/export", method = RequestMethod.POST)
    public void indexStatisticsExport(@RequestBody IndexStatisticsReq req, HttpServletResponse response) {
        String fileName = ExcelExportUtil.buildName("子后台首页统计导出");
        ExcelExportUtil.setResponseContent(response, fileName);
        try (ServletOutputStream out = response.getOutputStream()) {
            IndexStatisticsVO vos = service.indexStatistics(req);
            if (vos == null) {
                HSSFWorkbook book = new HSSFWorkbook();
                book.createSheet("null");
                book.write(out);
            } else {
                vos.setExchangeOrderAllPassRateTotal(vos.getExchangeOrderAllPassRateTotal());
                vos.setExchangeOrderHJPassRate(vos.getExchangeOrderHJPassRate());
                vos.setExchangeOrderKSLZPassRate(vos.getExchangeOrderKSLZPassRate());
                vos.setExchangeOrderPGDYPassRate(vos.getExchangeOrderPGDYPassRate());
                ExcelExportUtil.exportWorkbook(Collections.singletonList(vos)).write(out);
            }
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
        }
    }
}