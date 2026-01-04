package com.zxtx.hummer.common.controller;

import com.zxtx.hummer.common.service.SysAuditService;
import com.zxtx.hummer.common.utils.PageUtils;
import com.zxtx.hummer.common.utils.R;
import com.zxtx.hummer.common.vo.PageReq;
import com.zxtx.hummer.common.vo.SysAuditReq;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Controller
@ApiIgnore
@RequestMapping("/common/sysAudit")
public class SysAuditController {


    @Autowired
    private SysAuditService sysAuditService;


    @GetMapping("/page/list")
    @RequiresPermissions("common:sysAudit:sysAudit")
    public String SysAuditController() {
        return "common/sysAudit/sysAudit";
    }

    @GetMapping("/list")
    @ResponseBody
    @RequiresPermissions("common:sysAudit:sysAudit")
    public PageUtils pageList(SysAuditReq sysAuditReq, PageReq pageReq) {

        return sysAuditService.lsSysAudits(sysAuditReq, pageReq);
    }


    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("common:sysAudit:delete")
    public R remove(Long id) {
        if (sysAuditService.delById(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    @RequiresPermissions("common:sysAudit:delete")
    public R batchRemove(@RequestParam("ids[]") List<Long> ids) {

        int r = sysAuditService.delByIds(ids);
        if (r > 0) {
            return R.ok();
        }
        return R.error();
    }


}
