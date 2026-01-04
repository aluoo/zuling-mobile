package com.zxtx.hummer.common.controller;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.config.Constant;
import com.zxtx.hummer.common.config.HummerVueConfig;
import com.zxtx.hummer.common.domain.DictDO;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.Query;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-29 18:28:07
 */

@Controller
@Api(tags = "字典功能")
public class DictController extends BaseController {
    @Autowired
    private DictService dictService;
    @Autowired
    HummerVueConfig hummerVueConfig;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ResponseBody
    @ApiOperation("列表")
    @GetMapping("/api/dict/list")
    @RequiresPermissions("common:dict:dict")
    public Response<PageUtils<DictDO>> list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        Page<Object> page = PageHelper.startPage(query.getOffset(), query.getLimit());
        List<DictDO> dictList = dictService.list(query);
        if (CollUtil.isEmpty(dictList)) {
            return Response.ok(PageUtils.emptyPage());
        }
        return Response.ok(new PageUtils<>(dictList, page.getTotal()));
        // int total = dictService.count(query);
        // PageUtils pageUtils = new PageUtils(dictList, total);
        // return Response.ok(pageUtils);
    }

    /**
     * 保存
     */
    @ResponseBody
    @ApiOperation("保存")
    @PostMapping("/api/dict/save")
    @RequiresPermissions("common:dict:add")
    public Response save(@RequestBody DictDO dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (dictService.save(dict) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }

    /**
     * 修改
     */
    @ResponseBody
    @ApiOperation("修改")
    @PostMapping("/api/dict/update")
    @RequiresPermissions("common:dict:edit")
    public Response update(@RequestBody DictDO dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        dictService.update(dict);
        return Response.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/api/dict/remove")
    @ResponseBody
    @RequiresPermissions("common:dict:remove")
    public Response remove(@RequestBody DictDO dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        if (dictService.remove(dict.getId()) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }

    /**
     * 删除
     */
    @PostMapping("/api/dict/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:dict:batchRemove")
    public Response batchRemove(@RequestBody DictDO dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return Response.failed("演示系统不允许修改,完整体验请部署程序");
        }
        dictService.batchRemove(dict.getIds());
        return Response.ok();
    }

    @GetMapping("/api/dict/type")
    @ResponseBody
    public List<DictDO> listType() {
        return dictService.listType();
    }

    ;

    @ResponseBody
    @GetMapping("/api/dict/list/{type}")
    public List<DictDO> listByType(@PathVariable("type") String type) {
        // 查询列表数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", type);
        List<DictDO> dictList = dictService.list(map);
        return dictList;
    }

    @ResponseBody
    @GetMapping("/api/dict/listOperators")
    public Response listOpertors() {
        // 查询列表数据
        return Response.ok(dictService.listOperators());
    }

}