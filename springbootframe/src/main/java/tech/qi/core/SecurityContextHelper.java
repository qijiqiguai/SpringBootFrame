package tech.qi.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.qi.entity.User;


/**
 * @author wangqi
 */
public class SecurityContextHelper {

    private static final Logger logger = LoggerFactory.getLogger(SecurityContextHelper.class);

    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        // principal object is either null or represents anonymous user -
        // neither of which our domain User object can represent - so return null
        return null;
    }

    /**
     * 获取当前用户信息,如果没用用户信息，返回匿名用户
     * @return
     */
    public static User getCurrentUserWithAnonymous() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && principal instanceof User) {
                return (User) principal;
            }
            if(principal != null){
                //此时为匿名用户，username=anonymousUser
                User u = new User();
                u.setId(0L);
                u.setUsername(principal.toString());
                return u;
            }
        }catch (Exception e) {
            logger.error("获取当前用户信息异常",e);
        }
        return null;
    }

    /**
     * Utility method to determine if the current user is logged in /
     * authenticated.
     * <p/>
     * Equivalent of calling:
     * <p/>
     * <code>getCurrentUser() != null</code>
     *
     * @return if user is logged in
     */
    public static boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
}
