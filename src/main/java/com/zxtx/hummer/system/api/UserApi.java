package com.zxtx.hummer.system.api;

import com.zxtx.hummer.common.controller.BaseController;
import com.zxtx.hummer.common.utils.R;
import com.zxtx.hummer.system.domain.UserDO;
import com.zxtx.hummer.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/sys/userapi")
@Controller
public class UserApi extends BaseController {

    @Autowired
    UserService userService;

    @PostMapping("/save")
    @ResponseBody
    R save(UserDO user) {

        if (userService.save(user) > 0) {
            return R.ok();
        }
        return R.error();
    }


    @PostMapping("/removeByEmployee")
    @ResponseBody
    R removeByEmployee(Long employeeId) {
        if (userService.removeByEmployeeId(employeeId) > 0) {
            return R.ok();
        }
        return R.error();
    }
}
