package com.zxtx.hummer.system.controller;

import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.config.BootdoConfig;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.domain.Tree;
import com.zxtx.hummer.common.exception.BizError;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.service.FileService;
import com.zxtx.hummer.common.utils.R;
import com.zxtx.hummer.system.domain.MenuDO;
import com.zxtx.hummer.system.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Api(tags = "API登录")
public class ApiLoginController extends BaseController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    MenuService menuService;
    @Autowired
    FileService fileService;
    @Autowired
    BootdoConfig bootdoConfig;
    @Autowired
    DictService dictService;

    @RequestMapping("/filterError")
    @ResponseBody
    @ApiOperation("JTWTOKEN错误拦截")
    Response filterError() {
        return Response.failed(BizError.TOKEN_NOT_EXIST);
    }

    @GetMapping("/user/permission")
    @ResponseBody
    @ApiOperation("用户权限")
    Response permission() {
        List<Tree<MenuDO>> menus = menuService.listUserMenuTree(getUserId());
        return Response.ok(menus);
    }


}
