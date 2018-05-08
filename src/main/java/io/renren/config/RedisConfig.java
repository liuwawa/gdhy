package io.renren.config;

import io.renren.config.redis.CacheRedisConfig;
import io.renren.config.redis.TokenRedisConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 *
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017/12/12 16:00:00
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.cache.expiration}")
    private Long cacheDefaultExpiration;

    @Bean
    public RedisTemplate<String, Object> cacheRedisTemplate(CacheRedisConfig config) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(config.getHost());
        factory.setPort(config.getPort());
        factory.setPassword(config.getPassword());
        factory.setDatabase(config.getDatabase());
        factory.setTimeout(config.getTimeout());
        factory.afterPropertiesSet();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> tokenRedisTemplate(TokenRedisConfig config) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(config.getHost());
        factory.setPort(config.getPort());
        factory.setPassword(config.getPassword());
        factory.setDatabase(config.getDatabase());
        factory.setTimeout(config.getTimeout());
        factory.afterPropertiesSet();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(@Qualifier("cacheRedisTemplate") RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        redisCacheManager.setDefaultExpiration(cacheDefaultExpiration);
        return redisCacheManager;
    }
}
