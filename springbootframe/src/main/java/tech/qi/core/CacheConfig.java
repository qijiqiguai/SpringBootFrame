package tech.qi.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wangqi
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    @Autowired
    RedisTemplate redisTemplate;

    private final Map<String, Long> expires = new HashMap<>();
    private final long cacheDefaultTimeout = 60;
    private final String prefix = "SpringBootFrame:";

    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // 对不同的缓存设置超时时间,目前是统一的超时时间
        cacheManager.setExpires(expires);
        // Sets the default expire time (in seconds)
        cacheManager.setDefaultExpiration(cacheDefaultTimeout);
        cacheManager.setCachePrefix(new DefaultRedisCachePrefix(prefix));
        return cacheManager;
    }
}
