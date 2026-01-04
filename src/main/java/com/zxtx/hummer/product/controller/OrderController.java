package com.zxtx.hummer.product.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.product.domain.request.ProductOrderQueryReq;
import com.zxtx.hummer.product.domain.response.ProductOrderVO;
import com.zxtx.hummer.product.service.OrderManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/20
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "手机订单管理")
@RequestMapping("/api/product/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderManageService orderManageService;

    @ApiOperation("订单列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Response<PageUtils<ProductOrderVO>> listOrder(@RequestBody ProductOrderQueryReq req) {
        return Response.ok(orderManageService.listOrder(req));
    }
}