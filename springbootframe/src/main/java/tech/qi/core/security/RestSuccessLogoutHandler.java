package tech.qi.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import tech.qi.core.ServiceException;
import tech.qi.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author wangqi
 */
@Component
public class RestSuccessLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    /**
     * Custom LogoutSuccess Handler for Rest APIs.
     * <p/>
     * After successful logout, unbind clientId/deviceToken from user/getui alias. so that further notice will not deliver to them.
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String clientId = request.getParameter("clientId");
        String deviceToken = request.getParameter("deviceToken");
        String deviceTypeParam = request.getParameter("deviceType");
        if (authentication != null && authentication.getPrincipal() != null) {
            User user = (User) authentication.getPrincipal();
        }else {
            throw new ServiceException("No logged in yet, no need logout...");
        }
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.write("Logout OK");
        writer.flush();
    }

    private void tryUnBindClient(User user,int userType,String deviceTypeParam,String clientId,String deviceToken) {

    }
}
