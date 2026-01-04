package com.zxtx.hummer.order.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.product.domain.dto.OrderLogDTO;
import com.zxtx.hummer.product.domain.request.OrderLogQueryReq;
import com.zxtx.hummer.product.service.OrderLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 订单操作日志表 前端控制器
 * </p>
 *
 * @author L
 * @since 2024-03-07
 */
@Slf4j
@RestController
@Api(tags = "手机订单管理")
@RequestMapping("/api/mobile/order/log")
public class MbOrderLogController extends BaseController {
    @Autowired
    OrderLogService service;

    @ApiOperation("订单日志-全部")
    @RequestMapping(value = "/list/all", method = RequestMethod.POST)
    public Response<List<OrderLogDTO>> listAllLogs(@RequestBody OrderLogQueryReq req) {
        return Response.ok(service.listAllLogsByOrderId(req));
    }

    @ApiOperation("订单日志-分页")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<OrderLogDTO>> listLogsPage(@RequestBody OrderLogQueryReq req) {
        return Response.ok(service.listLogsByOrderId(req));
    }
}