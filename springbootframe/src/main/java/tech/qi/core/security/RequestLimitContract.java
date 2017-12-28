package tech.qi.core.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tech.qi.core.ServiceException;
import tech.qi.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author wangqi
 */
@Aspect
@Component
public class RequestLimitContract {

    private static final Logger logger = LoggerFactory.getLogger(RequestLimitContract.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void pointCut() {

    }


    @Before("pointCut() && @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws ServiceException {

        try {
//            Object[] args = joinPoint.getArgs();
//            HttpServletRequest request = null;
//            for (int i = 0; i < args.length; i++) {
//                if (args[i] instanceof HttpServletRequest) {
//                    request = (HttpServletRequest) args[i];
//                    break;
//                }
//            }
//            if (request == null) {
//                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
//            }
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

            String ip = IPAddressUtil.getClientIP(request);
            String url = request.getRequestURL().toString();
            String key = "Temp:RequestLimit:".concat(url).concat(ip);
            long count = redisTemplate.opsForValue().increment(key, 1);
            if (count == 1) {
                redisTemplate.expire(key, limit.time(), TimeUnit.MILLISECONDS);
            }
            if (count > limit.count()) {
                String message = "用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]";
                logger.info(message);
                throw new ServiceException(message);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("发生异常: ", e);
        }
    }
}
