package com.zxtx.hummer.common.lock;

import io.lettuce.core.RedisAsyncCommandsImpl;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.SetArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Component
public class MyDisReentrantLock {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static String clientId;

    private static final Logger logger = LoggerFactory.getLogger(MyDisReentrantLock.class);

    static {
        try {
            clientId = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param key
     * @param lessTime
     * @param timeUnit
     * @return
     */
    public boolean lock(String key, long lessTime, TimeUnit timeUnit) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            RedisAsyncCommandsImpl jedis = (RedisAsyncCommandsImpl) redisConnection.getNativeConnection();
            SetArgs setArgs = new SetArgs();
            setArgs.px(timeUnit.toMillis(lessTime));
            setArgs.nx();
            RedisFuture future = jedis.set(("lock:" + key).getBytes(), clientId.getBytes(), setArgs);
            try {
                Object o = future.get();
                logger.info("锁获取结果:{}", o);
                return "OK".equals(o);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            return false;
        });
    }

    /**
     * @param key
     * @return
     */
    public boolean unLock(String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
//            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
//            Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(key),
//                    Collections.singletonList(clientId));
//            return "OK".equals(result);
            RedisAsyncCommandsImpl jedis = (RedisAsyncCommandsImpl) redisConnection.getNativeConnection();
            RedisFuture future = jedis.eval(RELEASE_LOCK_SCRIPT, ScriptOutputType.INTEGER, key.getBytes(), clientId.getBytes());
            try {
                Object o = future.get();
                logger.info("锁释放结果：{}", o);
                return "0".equals(o);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
            return false;
        });
    }

    public boolean unLockKey(String key) {
        return redisTemplate.delete("lock:" + key);
    }
}
