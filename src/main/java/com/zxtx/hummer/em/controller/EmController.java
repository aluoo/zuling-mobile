package com.zxtx.hummer.em.controller;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.common.Response;
import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.exception.BaseException;
import com.zxtx.hummer.common.oss.OSSBucketEnum;
import com.zxtx.hummer.common.oss.OSSService;
import com.zxtx.hummer.common.oss.OssFileNameStrategy;
import com.zxtx.hummer.common.utils.ExcelExportUtil;
import com.zxtx.hummer.common.utils.FileUtil;
import com.zxtx.hummer.common.utils.ShiroUtils;
import com.zxtx.hummer.common.vo.PageReq;
import com.zxtx.hummer.company.service.CompanyService;
import com.zxtx.hummer.dept.domain.Dept;
import com.zxtx.hummer.dept.service.DeptService;
import com.zxtx.hummer.em.dao.mapper.EmployeeMapper;
import com.zxtx.hummer.em.domain.Employee;
import com.zxtx.hummer.em.domain.EmployeeOperateLog;
import com.zxtx.hummer.em.service.EmService;
import com.zxtx.hummer.em.service.EmployeeAncestorSearchService;
import com.zxtx.hummer.em.service.EmployeeOperateLogService;
import com.zxtx.hummer.em.vo.EmLsReq;
import com.zxtx.hummer.em.vo.EmLsRps;
import com.zxtx.hummer.em.vo.OperateLogVo;
import com.zxtx.hummer.em.vo.OperateQueryReq;
import com.zxtx.hummer.system.dao.mapper.UserRoleMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@Api(tags = "员工管理")
@RequestMapping("api/employee")
@Slf4j
public class EmController {

    @Autowired
    private EmService emService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    DeptService deptService;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    private OSSService ossService;
    @Autowired
    private EmployeeOperateLogService employeeOperateLogService;
    @Autowired
    private EmployeeAncestorSearchService employeeAncestorSearchService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Log(value = "重置密码", argsPos = {0, 1})
    @ApiOperation(value = "重置密码")
    @ResponseBody
    @PostMapping("/reset/pwd")
    @RequiresPermissions("employee:employee:reset:pwd")
    public Response<?> resetPassword(@RequestBody EmployeeOperateLog req) {
        if (req.getEmployeeId() == null) {
            throw new BaseException(-1, "员工ID不能为空");
        }
        emService.resetPwd(req.getEmployeeId());
        return Response.ok();
    }

    @Log(value = "员工登录解锁", argsPos = {0, 1})
    @ApiOperation(value = "员工登录解锁")
    @ResponseBody
    @PostMapping("/login/error/unlock")
    @RequiresPermissions("employee:employee:login:error:unlock")
    public Response<?> unlockEmployeeLoginError(@RequestBody EmployeeOperateLog req) {
        if (req.getEmployeeId() == null) {
            throw new BaseException(-1, "员工ID不能为空");
        }
        redisTemplate.delete(StrUtil.format("pangu:loginerror:{}", req.getEmployeeId()));
        return Response.ok();
    }

    @Log(value = "员工信息列表", argsPos = {0, 1})
    @ApiOperation(value = "员工信息列表")
    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("employee:employee:employee")
    public Response list(EmLsReq emLsReq, PageReq pageReq) {
        /*//当前登录人角色
        List<Long> roleIds = userRoleMapper.listRoleId(ShiroUtils.getUser().getUserId());
        //登录用户的部门
        Dept dept = deptService.getDeptById(ShiroUtils.getUser().getDeptId());
        if (ObjectUtil.isNotNull(dept)) {
            emLsReq.setDeptCodes(dept.getCode());
        }
        //超管 渠道运营 可查看所有
        if (roleIds.contains(1L) || roleIds.contains(104L)) {
            emLsReq.setDeptCodes(null);
        }*/
        return Response.ok(emService.pageList(emLsReq, pageReq));
    }


    /**
     * 导出
     *
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @Log(value = "导出员工信息", argsPos = {0, 1})
    @ApiOperation(value = "导出员工信息")
    @PostMapping(value = "/exportExcel")
    @RequiresPermissions("employee:employee:employee")
    public void exportExcel(EmLsReq emLsReq, PageReq pageReq, HttpServletResponse response) throws Exception {
        //当前登录人角色
        List<Long> roleIds = userRoleMapper.listRoleId(ShiroUtils.getUser().getUserId());
        //登录用户的部门
        Dept dept = deptService.getDeptById(ShiroUtils.getUser().getDeptId());
        if (ObjectUtil.isNotNull(dept)) {
            emLsReq.setDeptCodes(dept.getCode());
        }
        //超管 渠道运营 可查看所有
        if (roleIds.contains(1L) || roleIds.contains(104L)) {
            emLsReq.setDeptCodes(null);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "导出员工信息" + format.format(new Date().getTime()) + ".xls";
        ExcelExportUtil.setResponseContent(response, filename);
        OutputStream out = response.getOutputStream();
        try {
            List<EmLsRps> lsPerfCmpRep = emService.list(emLsReq, pageReq);
            ExcelExportUtil.exportWorkbook(lsPerfCmpRep).write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @ApiOperation(value = "员工上下线详情页")
    @GetMapping("/close/{mobile}")
    @ResponseBody
    Response close(@PathVariable String mobile) {
        EmLsReq emLsReq = new EmLsReq();
        emLsReq.setMobileNumber(mobile);
        PageReq pageReq = new PageReq();
        pageReq.setPage(1);
        pageReq.setPageSize(10);
        List<EmLsRps> emList = emService.list(emLsReq, pageReq);

        List<String> ancestorsList = Stream.of(emList.get(0).getAncestors().split(",")).collect(Collectors.toList());
        String ancestors = ancestorsList.get(0) + "," + ancestorsList.get(1);
        Employee bdEmployee = employeeMapper.selectBdByAncestors(ancestors);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("employee", emList.get(0));
        resultMap.put("bdName", ObjectUtil.isNotNull(bdEmployee) ? bdEmployee.getName() : "");
        return Response.ok(resultMap);
    }

    @ResponseBody
    @ApiOperation(value = "上下线传图片")
    @PostMapping("/upload")
    Response upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileUrl = null;
        try {
            fileUrl = ossService.put(OSSBucketEnum.AYCX_CUSTOMER.getName(), file, new OssFileNameStrategy() {
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


    @PostMapping("/close")
    @ApiOperation(value = "下线")
    @Log(value = "下线", argsPos = {0})
    @RequiresPermissions("employee:employee:close")
    @ResponseBody
    Response close(EmployeeOperateLog employeeOperateLog) {
        emService.closeEmployee(employeeOperateLog);
        return Response.ok();
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "注销")
    @Log(value = "注销", argsPos = {0})
    @RequiresPermissions("employee:employee:close")
    @ResponseBody
    Response cancel(EmployeeOperateLog employeeOperateLog) {
        emService.cancelEmployee(employeeOperateLog);
        return Response.ok();
    }

    @PostMapping("/online")
    @ApiOperation("上线")
    @Log(value = "上线", argsPos = {0})
    @RequiresPermissions("employee:employee:close")
    @ResponseBody
    Response online(EmployeeOperateLog employeeOperateLog) {
        emService.online(employeeOperateLog);
        return Response.ok();
    }

    @ResponseBody
    @GetMapping("/operate/list")
    @ApiOperation("上下线操作列表")
    public Response list(OperateQueryReq req) {
        List<OperateLogVo> resultList = employeeOperateLogService.selectPage(req);
        return Response.ok(resultList);
    }

    @ResponseBody
    @PostMapping("/search/ancestors/list")
    @ApiOperation("员工查询")
    public Response searchEmployeeAncestors(@RequestBody OperateQueryReq req) {
        if (StrUtil.isBlank(req.getMobileNumber())) {
            return Response.ok(new ArrayList<>());
        }
        return Response.ok(employeeAncestorSearchService.searchEmployeeAncestors(req.getMobileNumber()));
    }

    /**
     * 导出
     *
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @Log(value = "导出", argsPos = {0, 1})
    @ApiOperation("员工账号记录导出")
    @PostMapping(value = "/operate/exportExcel")
    public void exportExcel(OperateQueryReq req, HttpServletResponse response) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "员工账号记录" + format.format(new Date().getTime()) + ".xls";
        response.setContentType("application/ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "iso-8859-1"));
        OutputStream out = response.getOutputStream();
        try {
            req.setPage(1);
            req.setPageSize(5000);
            List<OperateLogVo> list = employeeOperateLogService.selectPage(req);
            ExcelExportUtil.exportWorkbook(list).write(out);
        } finally {
            out.close();
        }
    }

    @GetMapping("/operate/detail/{id}")
    @ApiOperation("员工账号记录详情")
    @ResponseBody
    Response detail(@PathVariable String id, Model model) {
        EmployeeOperateLog operateLog = employeeOperateLogService.getById(id);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("operateLog", operateLog);
        return Response.ok(resultMap);
    }

}