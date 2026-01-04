package com.zxtx.hummer.common.controller;

import com.zxtx.hummer.common.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class HealthController {


    @ApiOperation("健康检查")
    @GetMapping("/health-check")
    public R healthCheck() {
        return R.ok("Hello ,nvwa!");
    }


}
