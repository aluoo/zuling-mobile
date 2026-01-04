package com.zxtx.hummer.exchange.controller;

import cn.hutool.core.collection.CollUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.common.vo.DictMapVO;
import com.zxtx.hummer.exchange.domain.MbExchangeCustom;
import com.zxtx.hummer.exchange.domain.MbExchangeOrder;
import com.zxtx.hummer.exchange.domain.dto.ExchangeOrderCommissionBackDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangeOrderFillDTO;
import com.zxtx.hummer.exchange.domain.dto.ExchangePhoneVerifyDTO;
import com.zxtx.hummer.exchange.enums.ExchangeStatus;
import com.zxtx.hummer.exchange.req.ExchangeOrderReq;
import com.zxtx.hummer.exchange.service.MbExchangeCustomService;
import com.zxtx.hummer.exchange.service.MbExchangeOrderService;
import com.zxtx.hummer.exchange.vo.ExchangeOrderSummaryVO;
import com.zxtx.hummer.exchange.vo.ExchangeOrderVO;
import com.zxtx.hummer.exchange.vo.ExchangeOrderViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 换机晒单表 前端控制器
 * </p>
 *
 * @author L
 * @since 2024-03-27
 */
@Api(tags = "晒单审核管理")
@RestController
@RequestMapping("/api/exchange/order")
public class MbExchangeOrderController {

    @Autowired
    private MbExchangeOrderService mbExchangeOrderService;
    @Autowired
    private DictService dictService;
    @Autowired
    private MbExchangeCustomService customService;
    @ApiOperation("列表")
    @ResponseBody
    @PostMapping("/list")
    public Response<PageUtils<ExchangeOrderVO>> list(@RequestBody ExchangeOrderReq req) {
        List<ExchangeOrderVO> resultList = mbExchangeOrderService.selectPage(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @ApiOperation("客户手机安装记录列表")
    @ResponseBody
    @PostMapping("/custom/list")
    public Response<PageUtils<MbExchangeCustom>> customList(@RequestBody ExchangeOrderReq req) {
        return Response.ok(customService.selectInstallPage(req));
    }

    @ApiOperation("是否有待审核数据")
    @ResponseBody
    @GetMapping("/checkVoice")
    public Response checkVoice() {
        List<MbExchangeOrder> resultList = mbExchangeOrderService.lambdaQuery().in(MbExchangeOrder::getStatus,
                Arrays.asList(ExchangeStatus.SYS_Fail.getCode(),ExchangeStatus.SYS_PASS.getCode())).list();

        if(CollUtil.isEmpty(resultList)){
            return Response.ok(false);
        }
        return Response.ok(true);
    }

    @ApiOperation("晒单总计")
    @ResponseBody
    @PostMapping("/summary")
    public Response<ExchangeOrderSummaryVO> summary (@RequestBody ExchangeOrderReq req) {
        return Response.ok(mbExchangeOrderService.orderSummary(req));
    }

    @ApiOperation("详情")
    @ResponseBody
    @GetMapping("/view")
    public Response<ExchangeOrderViewVO> view(Long id) {
        return Response.ok(mbExchangeOrderService.view(id));
    }

    @ApiOperation("订单佣金追回")
    @ResponseBody
    @PostMapping("/commissionBack")
    @RequiresPermissions("exchange:order:commission:back")
    @Log(value = "订单佣金追回", argsPos = {0})
    public Response<?> commissionBack(@RequestBody ExchangeOrderCommissionBackDTO req) {
        mbExchangeOrderService.commissionBack(req);
        return Response.ok();
    }

    @ApiOperation("晒单审核原因")
    @ResponseBody
    @GetMapping("/reason")
    public Response<DictMapVO> getReason(Long id) {
        return Response.ok(dictService.getNameMap("phone_order_reason"));
    }

    @ApiOperation("晒单审核")
    @ResponseBody
    @PostMapping("/check")
    @Log(value = "晒单审核", argsPos = {0})
    public Response check(@RequestBody ExchangePhoneVerifyDTO dto) {
        mbExchangeOrderService.check(dto);
        return Response.ok();
    }

    @ApiOperation("单号回填")
    @ResponseBody
    @PostMapping("/fillSn")
    @RequiresPermissions("exchange:order:fillSn")
    @Log(value = "单号回填", argsPos = {0})
    public Response fillSn(@RequestBody ExchangeOrderFillDTO dto) {
        ValidatorUtil.validateBean(dto);
        mbExchangeOrderService.fillSn(dto);
        return Response.ok();
    }


    @ApiOperation("回退审核状态")
    @Log(value = "回退审核状态", argsPos = {0})
    @RequiresPermissions("exchange:order:rollback")
    @RequestMapping(value = "/return/order/status", method = RequestMethod.POST)
    public Response<?> rollbackOrderStatus(@RequestBody ExchangePhoneVerifyDTO dto) {
        mbExchangeOrderService.rollbackOrderStatus(dto);
        return Response.ok();
    }

    /**
     * 导出
     *
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation("导出晒单信息")
    @Log(value = "导出晒单信息", argsPos = {0, 1})
    @PostMapping(value = "/export")
    public void exportWithDraw(@RequestBody ExchangeOrderReq req, HttpServletResponse response) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "晒单" + format.format(new Date().getTime()) + ".xlsx";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "iso-8859-1"));
        OutputStream out = response.getOutputStream();
        try {
            req.setPage(1);
            req.setPageSize(100000);
            List<ExchangeOrderVO> resultList = mbExchangeOrderService.selectPage(req);
            ExcelExportUtil.exportWorkbook(resultList).write(out);
        } finally {
            out.close();
        }
    }

}