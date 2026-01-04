package com.zxtx.hummer.cms.controller;

import com.zxtx.hummer.cms.domain.dto.ArticleDTO;
import com.zxtx.hummer.cms.domain.response.ArticleVO;
import com.zxtx.hummer.cms.service.ArticleService;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/5/15
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@RestController
@Api(tags = "内容管理")
@RequestMapping("/api/product")
public class CmsController extends BaseController {
    @Autowired
    private ArticleService articleService;

    @ApiOperation("文章列表")
    @RequestMapping(value = "/article/list", method = RequestMethod.POST)
    @RequiresPermissions("cms:article:list")
    public Response<PageUtils<ArticleVO>> listArticle(@RequestBody ArticleDTO req) {
        return Response.ok(articleService.listPage(req));
    }

    @ApiOperation("添加文章")
    @Log(value = "内容管理-添加文章", argsPos = {0})
    @RequiresPermissions("cms:article:add")
    @RequestMapping(value = "/article/add", method = RequestMethod.POST)
    public Response<?> add(@RequestBody ArticleDTO req) {
        articleService.save(req);
        return Response.ok();
    }

    @ApiOperation("编辑文章")
    @Log(value = "内容管理-编辑文章", argsPos = {0})
    @RequiresPermissions("cms:article:edit")
    @RequestMapping(value = "/article/edit", method = RequestMethod.POST)
    public Response<?> edit(@RequestBody ArticleDTO req) {
        articleService.edit(req);
        return Response.ok();
    }

    @ApiOperation("发布/隐藏文章")
    @Log(value = "内容管理-发布/隐藏文章", argsPos = {0})
    @RequiresPermissions("cms:article:activated")
    @RequestMapping(value = "/article/switch/activated", method = RequestMethod.POST)
    public Response<?> switchActivated(@RequestBody ArticleDTO req) {
        articleService.switchActivated(req);
        return Response.ok();
    }

    @ApiOperation("发布/删除文章")
    @Log(value = "内容管理-删除文章", argsPos = {0})
    @RequiresPermissions("cms:article:remove")
    @RequestMapping(value = "/article/remove", method = RequestMethod.POST)
    public Response<?> remove(@RequestBody ArticleDTO req) {
        articleService.removeById(req.getId());
        return Response.ok();
    }
}