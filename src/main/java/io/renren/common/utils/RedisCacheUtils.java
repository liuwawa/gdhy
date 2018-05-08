package io.renren.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-07-17 21:12
 */
@Component
public class RedisCacheUtils {
    @Autowired
    @Qualifier("cacheRedisTemplate") private RedisTemplate<String, Object> redisTemplate;

    /**  默认过期时长，单位：秒 */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**  不设置过期时长 */
    public final static long NOT_EXPIRE = -1;

    /**
     * 缓存数据，并设置缓存时间
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key, Object value, long expire){
        redisTemplate.opsForValue().set(key, value);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 使用默认过期时长缓存数据
     * @param key
     * @param value
     */
    public void set(String key, Object value){
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 取出数据并设置过期时间
     * @param key
     * @param expire
     * @param <T>
     * @return
     */
    public <T> T get(String key, long expire) {
        Object value = redisTemplate.opsForValue().get(key);
        if(expire != NOT_EXPIRE){
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
        return (T) value;
    }

    /**
     * 获取用户信息
     * @param key
     * @return
     */
    public <T> T get(String key) {
        return get(key, NOT_EXPIRE);
    }

    /**
     * 清除缓存
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
