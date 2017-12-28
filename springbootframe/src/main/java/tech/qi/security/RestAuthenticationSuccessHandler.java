package tech.qi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tech.qi.dal.UserService;
import tech.qi.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * @author wangqi
 */
@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public RestAuthenticationSuccessHandler() {
        // 指定登陆成功之后重定向的路径
        super("/api/v1/auth/refresh");
    }

    @Autowired
    private UserService userService;

    @Value("${app.rest.session-timeout}")
    private int restSessionTimeout;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 绑定设备
        User user = (User) authentication.getPrincipal();

        String clientId = request.getParameter("clientId");
        String deviceToken = request.getParameter("deviceToken");
        String deviceTypeParam = request.getParameter("deviceType");
        //Override server.session-timeout for Rest Login
        request.getSession().setMaxInactiveInterval(restSessionTimeout);

        userService.userLogin(request, user,clientId, deviceToken, deviceTypeParam);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean validUserTypeAndTryBindClient(User user, int userType, String deviceTypeParam, String clientId, String deviceToken) {
        return false;
    }
}
