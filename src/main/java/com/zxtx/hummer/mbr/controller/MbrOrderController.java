package com.zxtx.hummer.mbr.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.ValidatorUtil;
import com.zxtx.hummer.mbr.domain.dto.HwzOrderDetailDTO;
import com.zxtx.hummer.mbr.domain.enums.MbrOrderStatusEnum;
import com.zxtx.hummer.mbr.req.MbrOrderMarkReq;
import com.zxtx.hummer.mbr.req.MbrOrderReq;
import com.zxtx.hummer.mbr.response.MbrOrderVO;
import com.zxtx.hummer.mbr.service.MbrOrderService;
import com.zxtx.hummer.withdraw.constant.WithdrawCardIllegalTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjian
 * @Description
 * @Date 2025/5/6
 */
@Api(tags = "租机单管理模块")
@RestController
public class MbrOrderController extends BaseController {
    @Autowired
    private MbrOrderService mbrOrderService;

    @ApiOperation("租机单列表")
    @PostMapping("/api/mbr/order/list")
    public Response<PageUtils<MbrOrderVO>> orderList(@RequestBody MbrOrderReq req) {
        return Response.ok(mbrOrderService.listOrder(req));
    }

    @ApiOperation("状态列表")
    @GetMapping("/api/mbr/status/list")
    public Response<List<Map>> statusList() {
        List<Map> resultList = new ArrayList<>();
        for (MbrOrderStatusEnum typeEnum : MbrOrderStatusEnum.values()) {
            Map<Integer, String> resultMap = new HashMap();
            resultMap.put(typeEnum.getCode(),typeEnum.getDesc());
            resultList.add(resultMap);
        }
        return Response.ok(resultList);
    }

    @ApiOperation("租机单详情")
    @GetMapping("/api/mbr/order/detail/{id}")
    public Response<HwzOrderDetailDTO> orderDetail(@PathVariable Long id) {
        return Response.ok(mbrOrderService.orderDetail(id));
    }

    @ApiOperation("标记异常")
    @RequiresPermissions("mbr:order:mark")
    @PostMapping("/api/mbr/order/mark/abnormal")
    public Response<?> markAbnormal(@RequestBody MbrOrderMarkReq req) {
        ValidatorUtil.validateBean(req);
        mbrOrderService.markAbnormal(req);
        return Response.ok();
    }
}