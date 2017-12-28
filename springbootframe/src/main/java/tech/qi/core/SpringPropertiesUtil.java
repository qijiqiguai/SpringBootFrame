package tech.qi.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.EnvironmentCapable;

/**
 *
 * @author wangqi
 */
public class SpringPropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(SpringPropertiesUtil.class);

    public static String getProperty(String key){
        try {
            EnvironmentCapable environmentCapable = WebContextHolder.getInstance().getApplicationContext();
            return environmentCapable.getEnvironment().getProperty(key);
        } catch (Exception e) {
            logger.error("获取spring配置key="+key+"异常", e);
            return null;
        }
    }

}
