package com.zxtx.hummer.exchange.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.exchange.domain.dto.ExchangeEmployeePackageDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangeEmployeeUpdateDTO;
import com.zxtx.hummer.exchange.req.ExchangeEmployeeInfoReq;
import com.zxtx.hummer.exchange.service.MbExchangeEmployeeService;
import com.zxtx.hummer.exchange.vo.ExchangeEmployeeInfoVO;
import com.zxtx.hummer.exchange.vo.ExchangeEmployeeViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 合伙人换机包 前端控制器
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */

@Slf4j
@Api(tags = "APP拉新功能管理")
@RestController
@RequestMapping("/api/exchange")
public class MbExchangeEmployeeController {
    @Autowired
    private MbExchangeEmployeeService mbExchangeEmployeeService;

    @ApiOperation("代理账号管理")
    @ResponseBody
    @PostMapping("/employee/list")
    public Response<PageUtils<ExchangeEmployeeInfoVO>> list(@RequestBody ExchangeEmployeeInfoReq req) {
        List<ExchangeEmployeeInfoVO> resultList = mbExchangeEmployeeService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @ApiOperation("代理账号管理")
    @ResponseBody
    @PostMapping("/sumtoTal")
    public Response sumtoTal(@RequestBody ExchangeEmployeeInfoReq req) {
        List<ExchangeEmployeeInfoVO> resultList = mbExchangeEmployeeService.selectPage(req);
        return Response.ok(resultList.stream().mapToLong(ExchangeEmployeeInfoVO::getAbleBalance).sum());
    }

    @ApiOperation("代理账号管理导出")
    @Log(value = "代理账号管理导出", argsPos = {0, 1})
    @PostMapping(value = "/export")
    public void exportWithDraw(@RequestBody ExchangeEmployeeInfoReq req, HttpServletResponse response) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "代理账号管理" + format.format(new Date().getTime()) + ".xls";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "iso-8859-1"));
        OutputStream out = response.getOutputStream();
        try {
            req.setPage(1);
            req.setPageSize(10000);
            List<ExchangeEmployeeInfoVO> resultList = mbExchangeEmployeeService.selectPage(req);
            ExcelExportUtil.exportWorkbook(resultList).write(out);
        } finally {
            out.close();
        }
    }
    @ApiOperation("代理账号管理编辑")
    @ResponseBody
    @PostMapping("/employee/update")
    public Response update(@RequestBody ExchangeEmployeeUpdateDTO dto) {
       mbExchangeEmployeeService.update(dto);
        return Response.ok();
    }

    @ApiOperation("代理账号查看数据")
    @ResponseBody
    @GetMapping("/employee/view")
    public Response<ExchangeEmployeeViewVO> update(Long employeeId) {
        return Response.ok(mbExchangeEmployeeService.view(employeeId));
    }

    @ApiOperation("拉新配置")
    @ResponseBody
    @PostMapping("/employee/package")
    public Response updatePackage (@RequestBody ExchangeEmployeePackageDTO dto) {
        mbExchangeEmployeeService.updatePackage(dto);
        return Response.ok();
    }



}
