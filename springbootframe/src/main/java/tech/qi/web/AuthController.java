package tech.qi.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tech.qi.core.MessageHandler;
import tech.qi.core.SecurityContextHelper;
import tech.qi.dal.UserService;
import tech.qi.entity.User;
import tech.qi.web.form.UserExistenceCheckForm;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import static tech.qi.core.Constants.*;



/**
 * @author wangqi
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @Autowired
    MessageHandler messageHandler;


    @RequestMapping(value = "/user/exists", method = RequestMethod.POST)
    public Map<String, Object> userCheck(@Valid @RequestBody UserExistenceCheckForm form) {
        Map<String, Object> map = new HashMap<>(3);
        User user = userService.getUserByName(form.getPhone());

        if (null != user) {
            map.put(REST_DATA_KEY, true);
            map.put(REST_MESSAGE_KEY, messageHandler.getMessage("user.register.nonconflict"));
        }else {
            map.put(REST_DATA_KEY, true);
            map.put(REST_MESSAGE_KEY, messageHandler.getMessage("user.register.conflict"));
        }
        map.put(REST_STATUS_KEY, 200);
        return map;
    }


    @Transactional
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public Map<String, Object> userRegister(@Valid @RequestBody String form, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    /**
     * 修改密码
     *
     * 老师和学生用户都适用
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "password/change", method = RequestMethod.POST)
    public Map<String, Object> changePassword(@Valid @RequestBody String form) {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    /**
     * 重置密码
     *
     * 必须先通过短信验证码校验 - 从Session里校验token, 并获取phone
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "password/reset", method = RequestMethod.POST)
    public Map<String, Object> resetPassword(@Valid @RequestBody String form) {
        Map<String, Object> map = new HashMap<>();

        return map;
    }

    /**
     * Verify the code, store info in session and return token
     *
     * @param form
     * @return
     */
    @RequestMapping(value = "code/verify", method = RequestMethod.POST)
    public Map<String, Object> codeVerify(@Valid @RequestBody String form) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }



    @RequestMapping(value = "code/generate", method = RequestMethod.POST)
    public Map<String, Object> sendMsg(HttpServletRequest request, @Valid @RequestBody String form) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    /**
     * validate wx code, get access_token and unionid, generate oauth2 token and return the token
     * @return
     */
    @RequestMapping(value = "wx_code/validate", method = RequestMethod.POST)
    public Map<String, Object> wxCodeValidate(@Valid @RequestBody String oauth2CodeForm, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    /**
     * bind user with oauth2
     * @param form
     * @return
     */
    @Transactional
    @RequestMapping(value = "bind", method = RequestMethod.POST)
    public Map<String, Object> bindUser(@Valid @RequestBody String form, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @Transactional
    @RequestMapping(value = "bind/invitation", method = RequestMethod.POST)
    public Map<String, Object> bindInvitation(@RequestBody String form) {
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    @RequestMapping(value = "refresh",method = RequestMethod.GET)
    public Map<String, Object> refreshToken() {
        Map<String, Object> map = new HashMap<>();

        map.put(REST_DATA_KEY, true);
        map.put(REST_MESSAGE_KEY, messageHandler.getMessage("user.login.success"));
        map.put(REST_STATUS_KEY, 200);
        return map;
    }
}
