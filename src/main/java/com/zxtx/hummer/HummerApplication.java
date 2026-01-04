package com.zxtx.hummer;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableConfigurationProperties
@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.zxtx.hummer.**.dao")
@SpringBootApplication
@EnableCaching
@EnableSwaggerBootstrapUI
@EnableScheduling
public class HummerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HummerApplication.class, args);

        System.out.println(
                "##     ## ##     ## ##     ## ##     ## ######## ########  \n" +
                        "##     ## ##     ## ###   ### ###   ### ##       ##     ## \n" +
                        "##     ## ##     ## #### #### #### #### ##       ##     ## \n" +
                        "######### ##     ## ## ### ## ## ### ## ######   ########  \n" +
                        "##     ## ##     ## ##     ## ##     ## ##       ##   ##   \n" +
                        "##     ## ##     ## ##     ## ##     ## ##       ##    ##  \n" +
                        "##     ##  #######  ##     ## ##     ## ######## ##     ## "
        );

        System.out.println("Hummer启动成功!");

    }


}
