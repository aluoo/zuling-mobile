package com.zxtx.hummer.dept.controller;


import com.zxtx.hummer.common.annotation.Log;
import com.zxtx.hummer.common.utils.R;
import com.zxtx.hummer.dept.service.DeptService;
import com.zxtx.hummer.dept.vo.UpdateDeptReq;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@RequestMapping("emDept/")
@Controller
public class EmDeptController {

    @Autowired
    private DeptService deptService;


    @GetMapping("")
    @RequiresPermissions("emDept:emDept:emDept")
    String EmDeptController() {
        return "emDept/list";
    }

    @Log(value = "切换部门", argsPos = {0})
    @ResponseBody
    @PostMapping("/update")
    @RequiresPermissions("emDept:emDept:emDept")
    public R updateDept(@Valid UpdateDeptReq deptReq) {
        deptService.updateDept(deptReq);
        return R.ok();
    }
}
