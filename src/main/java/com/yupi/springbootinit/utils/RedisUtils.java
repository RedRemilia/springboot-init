package com.yupi.springbootinit.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final StringRedisTemplate stringRedisTemplate;

    public void set(String key, Object value) {
        stringRedisTemplate
                .opsForValue()
                .set(key, value.toString());
    }

    public void set(String key, Object value, int expire) {
        stringRedisTemplate
                .opsForValue()
                .set(key, value.toString(), expire, TimeUnit.SECONDS);
    }

    public Boolean setIfAbsent(String key, Object value) {
        return stringRedisTemplate
                .opsForValue()
                .setIfAbsent(key, value.toString());
    }

    public Boolean setIfAbsent(String key, Object value, int seconds) {
        return stringRedisTemplate
                .opsForValue()
                .setIfAbsent(key, value.toString(), seconds, TimeUnit.SECONDS);
    }

    public Boolean exist(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void expire(String key, int seconds) {
        stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    public void hset(String key, String field, Object value) {
        stringRedisTemplate.opsForHash().put(key, field, value == null ? null : value.toString());
    }

    public void hmset(String key, Map<String, ?> value) {
        // StringRedisTemplate 的 hash value 序列化要求 String：
        // 这里统一把每个 value 转成字符串，避免 Integer 等类型序列化时报 ClassCastException
        Map<String, String> raw = new HashMap<>(value.size());
        value.forEach((k, v) -> raw.put(k, v == null ? null : v.toString()));
        stringRedisTemplate.opsForHash().putAll(key, raw);
    }

    public void hdel(String key, String... field) {
        stringRedisTemplate.opsForHash().delete(key, (Object[]) field);
    }

    public Object hget(String key, String field) {
        return stringRedisTemplate.opsForHash().get(key, field);
    }

    public Long hIncrBy(String key, String field) {
        return stringRedisTemplate.opsForHash().increment(key, field, 1);
    }

}
