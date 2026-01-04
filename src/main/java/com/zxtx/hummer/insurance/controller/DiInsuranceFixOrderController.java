package com.zxtx.hummer.insurance.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.insurance.domain.DiInsuranceFixOrder;
import com.zxtx.hummer.insurance.domain.DiOption;
import com.zxtx.hummer.insurance.enums.DiFixOrderStatusEnum;
import com.zxtx.hummer.insurance.enums.DiOptionCodeEnum;
import com.zxtx.hummer.insurance.req.DiInsuranceFixOrderAmountReq;
import com.zxtx.hummer.insurance.req.DiInsuranceFixOrderAuditReq;
import com.zxtx.hummer.insurance.req.DiInsuranceFixReq;
import com.zxtx.hummer.insurance.service.DiInsuranceFixOrderService;
import com.zxtx.hummer.insurance.service.DiOptionService;
import com.zxtx.hummer.insurance.vo.DiInsuranceFixOrderDetailVO;
import com.zxtx.hummer.insurance.vo.DiInsuranceFixOrderVO;
import com.zxtx.hummer.product.service.ProductDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 数保报修订单 前端控制器
 * </p>
 *
 * @author L
 * @since 2024-06-06
 */
@Slf4j
@Api(tags = "碎屏险-报修订单")
@RestController
@RequestMapping("/api/di/insurance/fixOrder")
public class DiInsuranceFixOrderController {

    @Autowired
    DiInsuranceFixOrderService fixOrderService;
    @Autowired
    DiOptionService diOptionService;
    @Autowired
    ProductDictService productDictService;

    @ApiOperation("获取理赔订单关闭原因列表")
    @RequestMapping(value = "/list/reason/close", method = RequestMethod.GET)
    public Response<List<String>> listCloseReason() {
        return Response.ok(productDictService.getInsuranceFixOrderCloseReason());
    }

    @ApiOperation("获取理赔订单审核拒绝原因列表")
    @RequestMapping(value = "/list/reason/reject", method = RequestMethod.GET)
    public Response<List<String>> listRejectReason() {
        return Response.ok(productDictService.getInsuranceFixOrderRejectReason());
    }

    @ApiOperation("获取理赔资料审核拒绝原因列表")
    @RequestMapping(value = "/list/reason/data/reject", method = RequestMethod.GET)
    public Response<List<String>> listDataRejectReason() {
        return Response.ok(productDictService.getInsuranceFixOrderDataRejectReason());
    }

    @ApiOperation("获取理赔服务列表")
    @RequestMapping(value = "/list/fixServiceType", method = RequestMethod.POST)
    public Response<List<DiOption>> listFixServiceType() {
        return Response.ok(diOptionService.listByCode(DiOptionCodeEnum.FIXSERVICE.getCode()));
    }

    @ApiOperation("获取理赔项目列表")
    @RequestMapping(value = "/list/claimItem", method = RequestMethod.POST)
    public Response<List<DiOption>> listClaimItem() {
        return Response.ok(diOptionService.listByCode(DiOptionCodeEnum.FIXITEM.getCode()));
    }

    @ApiOperation("维修保单列表")
    @PostMapping("/list")
    public Response<PageUtils<DiInsuranceFixOrderVO>> list(@RequestBody DiInsuranceFixReq req) {
        return Response.ok(fixOrderService.listFixOrders(req));
    }


    @ApiOperation("保险订单审核")
    @ResponseBody
    @PostMapping("/fixCheck")
    public Response<?> fixCheck(@RequestBody DiInsuranceFixOrderAuditReq req) {
        ValidatorUtil.validateBean(req);
        fixOrderService.fixCheck(req);
        return Response.ok();
    }

    @ApiOperation("理赔材料审核")
    @ResponseBody
    @PostMapping("/fixData")
    public Response<?> fixData(@RequestBody DiInsuranceFixOrderAuditReq req) {
        ValidatorUtil.validateBean(req);
        fixOrderService.dataCheck(req);
        return Response.ok();
    }

    @ApiOperation("详情")
    @ResponseBody
    @GetMapping("/detail/{id}")
    public Response<DiInsuranceFixOrderDetailVO> detail(@PathVariable Long id) {
        return Response.ok(fixOrderService.detailFixOrder(id));
    }

    @ApiOperation("关闭报险单")
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    public Response<?> closeFixOrder(@RequestBody DiInsuranceFixOrderAuditReq req) {
        ValidatorUtil.validateBean(req);
        fixOrderService.close(req);
        return Response.ok();
    }

    @ApiOperation("维护档口价格")
    @RequestMapping(value = "/edit/newProductSkuStallPrice", method = RequestMethod.POST)
    public Response<BigDecimal> editNewProductSkuStallPrice(@RequestBody DiInsuranceFixOrderAmountReq req) {
        ValidatorUtil.validateBean(req);
        fixOrderService.editNewProductSkuStallPrice(req);
        return Response.ok(req.getAmount());
    }

    @ApiOperation("维护理赔金额")
    @RequestMapping(value = "/edit/settleAmount", method = RequestMethod.POST)
    public Response<BigDecimal> editSettleAmount(@RequestBody DiInsuranceFixOrderAmountReq req) {
        ValidatorUtil.validateBean(req);
        fixOrderService.editSettleAmount(req);
        return Response.ok(req.getAmount());
    }

    @ApiOperation("报修订单待审核-提醒")
    @RequestMapping(value = "/checkVoice/wait", method = RequestMethod.GET)
    public Response<Boolean> checkVoiceWait() {
        Long count = fixOrderService.lambdaQuery()
                .eq(DiInsuranceFixOrder::getStatus, DiFixOrderStatusEnum.WAIT.getCode())
                .count();
        return Response.ok(count > 0);
    }

    @ApiOperation("理赔材料待审核-提醒")
    @RequestMapping(value = "/checkVoice/data_wait", method = RequestMethod.GET)
    public Response<Boolean> checkVoiceDataWait() {
        Long count = fixOrderService.lambdaQuery()
                .eq(DiInsuranceFixOrder::getStatus, DiFixOrderStatusEnum.DATA_WAIT.getCode())
                .count();
        return Response.ok(count > 0);
    }
}