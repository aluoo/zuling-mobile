package com.zxtx.hummer.system.controller;

import cn.hutool.core.text.StrFormatter;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.oss.OSSBucketEnum;
import com.zxtx.hummer.common.oss.OSSService;
import com.zxtx.hummer.common.oss.OssFileNameStrategy;
import com.zxtx.hummer.common.service.DictService;
import com.zxtx.hummer.common.utils.FileUtil;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.Query;
import com.zxtx.hummer.common.utils.R;
import com.zxtx.hummer.system.domain.ProjectsDO;
import com.zxtx.hummer.system.service.ProjectsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/app/projects")
@Api(tags = "项目管理")
@Slf4j
public class ProjectsController {
    @Autowired
    private ProjectsService projectsService;
    @Autowired
    private OSSService ossService;
    @Autowired
    private DictService dictService;

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation("列表")
    public Response list(@RequestParam Map<String, Object> params) {
        //查询列表数据,buildCode为0的项目
        params.put("buildCode", "0");
        Query query = new Query(params);
        List<ProjectsDO> projectsList = projectsService.list(query);
        int total = projectsService.count(query);
        PageUtils pageUtils = new PageUtils(projectsList, total);
        return Response.ok(pageUtils);
    }

    @ResponseBody
    @ApiOperation("配置详情列表")
    @GetMapping("/listofson")
    public Response listofson(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<ProjectsDO> projectsList = projectsService.listofson(query);//projectCode必传
        int total = projectsService.countofson(query);
        PageUtils pageUtils = new PageUtils(projectsList, total);
        return Response.ok(pageUtils);
    }

    @ResponseBody
    @PostMapping("/uploadApk")
    Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileUrl = null;
        try {
            fileUrl = ossService.put(OSSBucketEnum.AYCX_APK.getName(), file, new OssFileNameStrategy() {
                @Override
                public String getFileName(MultipartFile file) {

                    Date now = new Date();
                    return StrFormatter.format("{}/{}", DateFormatUtils.format(now, "yyyy/MM/dd"), FileUtil.renameToUUID(file.getOriginalFilename()));
                }
            });

        } catch (IOException e) {
            log.error("文件上传失败！！", e);
            return Response.failed("文件上传失败！！");
        }

        log.info("fileUrl : {}", fileUrl);

        return Response.ok(fileUrl);
    }

    @GetMapping("/edit/{projectId}")
    @ApiOperation("详情")
    Response edit(@PathVariable("projectId") Integer projectId) {
        Map<String, Object> map = new HashMap<>();
        ProjectsDO projects = projectsService.get(projectId);
        map.put("projects", projects);
        return Response.ok(map);
    }

    @GetMapping("/detail/{projectId}")
    @ApiOperation("配置详情")
    Response detail(@PathVariable("projectId") Integer projectId) {
        Map<String, Object> map = new HashMap<>();
        ProjectsDO projects = projectsService.get(projectId);
        map.put("projects", projects);
        return Response.ok(map);
    }

    @GetMapping("/getbyid/{projectId}")
    @ApiOperation("根据ID获取")
    @ResponseBody
    R get(@PathVariable("projectId") Integer projectId) {
        ProjectsDO projects = projectsService.get(projectId);
        return R.ok(projects);
    }

    @GetMapping("/getbyprojectcode/{projectCode}")
    @ApiOperation("根据项目CODE获取")
    @ResponseBody
    Response getByCode(@PathVariable("projectCode") Integer projectCode) {
        ProjectsDO projects = projectsService.getByCode(projectCode);
        return Response.ok(projects);
    }


    /**
     * 保存
     */
    @ResponseBody
    @ApiOperation("保存")
    @PostMapping("/save")
    public Response save(@RequestBody ProjectsDO projects) {
        if (projectsService.save(projects) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }


    /**
     * 保存
     *
     * @throws Exception
     */
    @Log(value = "新增子项目", argsPos = {0})
    @ResponseBody
    @ApiOperation("新增子项目")
    @PostMapping("/saveofson")
    public Response saveofson(@RequestBody ProjectsDO projects) {
        if (projectsService.saveofson(projects) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }


    /**
     * 修改
     */
    @Log(value = "修改子项目", argsPos = {0})
    @ResponseBody
    @ApiOperation("修改子项目")
    @PostMapping("/update")
    public Response update(@RequestBody ProjectsDO projects) {
        projectsService.update(projects);
        return Response.ok();
    }


    /**
     * 删除
     */
    @Log(value = "删除子项目", argsPos = {0})
    @PostMapping("/remove")
    @ResponseBody
    @ApiOperation("删除子项目")
    public Response remove(Integer projectId) {
        if (projectsService.remove(projectId) > 0) {
            return Response.ok();
        }
        return Response.failed("操作失败");
    }


    /**
     * 删除
     */
    @Log(value = "批量删除子项目", argsPos = {0})
    @PostMapping("/batchRemove")
    @ResponseBody
    @ApiOperation("批量删除子项目")
    public Response remove(@RequestParam("ids[]") Integer[] projectIds) {
        projectsService.batchRemove(projectIds);
        return Response.ok();
    }

}