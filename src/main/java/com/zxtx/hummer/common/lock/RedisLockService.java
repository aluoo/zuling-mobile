package com.zxtx.hummer.common.lock;

import cn.hutool.core.util.StrUtil;
import com.zxtx.hummer.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author WangWJ
 * @Description
 * @Date 2024/6/14
 * @Copyright
 * @Version 1.0
 */
@Slf4j
@Component
public class RedisLockService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void redisLock(String key, Long id) {
        if (StrUtil.isBlank(key)) {
            return;
        }
        String lk = StrUtil.format("{}:{}", key, id);
        if (Boolean.TRUE.equals(redisTemplate.hasKey(lk))) {
            throw new BaseException(9999, "操作太频繁,请稍后操作");
        }
        redisTemplate.opsForValue().set(lk, id.toString(), 2, TimeUnit.SECONDS);
    }

    public void redisLock(String key, Long expireTime, TimeUnit timeUnit) {
        if (StrUtil.isBlank(key)) {
            return;
        }
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new BaseException(9999, "操作太频繁,请稍后操作");
        }
        redisTemplate.opsForValue().set(key, String.valueOf(System.currentTimeMillis()), expireTime, timeUnit);
    }
}