package com.zxtx.hummer.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author peng can
 * @date 2022/12/9 23:02
 */
@Component
@ConfigurationProperties(prefix = "bc.sign")
@Data
public class BcSignProperties {
    /**
     * 私玥
     */
    private String privateKey;

    /**
     * 公钥钥
     */
    private String publicKey;

    /**
     * 服务器地址
     */
    private String serverHost;


}
