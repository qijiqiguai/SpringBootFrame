package tech.qi.conf;

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
        super("/api/v2/auth/refresh");
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
        String idfa = request.getParameter("idfa");
        String deviceTypeParam = request.getParameter("deviceType");
        int userType = Integer.parseInt(request.getParameter("userType"));

        //Override server.session-timeout for Rest Login
        request.getSession().setMaxInactiveInterval(restSessionTimeout);

        if (!validUserTypeAndTryBindClient(user, userType, deviceTypeParam, clientId, deviceToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        userService.userLogin(request, user,clientId, deviceToken, idfa, deviceTypeParam, userType);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private boolean validUserTypeAndTryBindClient(User user, int userType, String deviceTypeParam, String clientId, String deviceToken) {

        return false;
    }
}
