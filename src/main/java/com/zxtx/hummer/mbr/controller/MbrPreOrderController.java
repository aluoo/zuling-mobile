package com.zxtx.hummer.mbr.controller;

import com.zxtx.hummer.common.Constants;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.config.HummerVueConfig;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.mbr.req.MbrPreOrderReq;
import com.zxtx.hummer.mbr.response.MbrPreOrderVO;
import com.zxtx.hummer.mbr.service.MbrPreOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chenjian
 * @Description
 * @Date 2025/5/6
 */
@Controller
@Api(tags = "进件单管理模块")
public class MbrPreOrderController {

    @Autowired
    private HummerVueConfig hummerVueConfig;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    MbrPreOrderService preOrderService;


    @RequestMapping("/mbr/pre/order/page")
    String preOrderPage (HttpServletRequest request) {
        String jwtTokenKey = String.format(Constants.LOGIN_USER_JWT_TOKEN, request.getSession().getId());
        String jwtToken = redisTemplate.opsForValue().get(jwtTokenKey);
        return "redirect:" + hummerVueConfig.getVueUrl() + "/mbr-pre-order?jwtToken=" + jwtToken;
    }

    @ApiOperation("进件单列表")
    @ResponseBody
    @PostMapping("/api/mbr/pre/order/list")
    public Response<PageUtils<MbrPreOrderVO>> preOrderList(@RequestBody MbrPreOrderReq req) {
        List<MbrPreOrderVO> resultList = preOrderService.settlePageList(req);
        return Response.ok(PageUtils.create(resultList));
    }

    @ResponseBody
    @ApiOperation("进件单详情")
    @GetMapping("/api/mbr/pre/order/detail/{id}")
    public Response<MbrPreOrderVO> preOrderDetail(@PathVariable Long id) {
        return Response.ok(preOrderService.preOrderDetail(id));
    }


}