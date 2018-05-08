package io.renren.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ITMX on 2017/12/22.
 */
@Component
@ConfigurationProperties(prefix = "spring.redis.cache")
public class CacheRedisConfig extends BaseRedisConfig {
}