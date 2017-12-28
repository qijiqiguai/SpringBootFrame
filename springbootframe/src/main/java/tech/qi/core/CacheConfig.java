package tech.qi.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 * @author wangqi
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    @Autowired
    RedisTemplate redisTemplate;

    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // 对不同的缓存设置超时时间,目前是统一的超时时间
        //cacheManager.setExpires(expires);
        // Sets the default expire time (in seconds)
//        cacheManager.setDefaultExpiration(cacheTimeout);
        //cacheManager.setCachePrefix();
        return cacheManager;
    }
}
