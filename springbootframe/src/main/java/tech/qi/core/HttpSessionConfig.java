package tech.qi.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author wangqi
 */
@EnableRedisHttpSession(redisNamespace = Constants.REDIS_SESSION_NAME)
public class HttpSessionConfig {

    @Primary
    @Bean
    public RedisOperationsSessionRepository sessionRepository(RedisConnectionFactory redisConnectionFactory) {
        RedisOperationsSessionRepository sessionRepository =
                new RedisOperationsSessionRepository(redisConnectionFactory);
        return sessionRepository;
    }

}
