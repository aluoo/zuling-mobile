package com.zxtx.hummer.hk.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.hk.domain.response.HkCommonPropertiesVO;
import com.zxtx.hummer.hk.service.HkCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangWJ
 * @Description
 * @Date 2025/8/1
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "号卡通用设置管理")
@RequestMapping("/api/hk/common")
public class HkCommonController extends BaseController {
    @Autowired
    HkCommonService service;

    @ApiOperation("获取号卡通用信息（运营商、供应商、订单状态）")
    @RequestMapping(value = "/properties", method = RequestMethod.POST)
    public Response<HkCommonPropertiesVO> commonPropertiesInfo() {

        return Response.ok(service.commonPropertiesInfo());
    }
}