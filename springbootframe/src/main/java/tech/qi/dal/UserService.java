package tech.qi.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tech.qi.core.Constants;
import tech.qi.core.MessageHandler;
import tech.qi.core.ServiceException;
import tech.qi.dal.repository.BaseRepository;
import tech.qi.dal.repository.RoleRepository;
import tech.qi.dal.repository.UserRepository;
import tech.qi.entity.City;
import tech.qi.entity.User;
import tech.qi.entity.security.Role;
import tech.qi.entity.security.UserLogin;
import tech.qi.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageHandler messageHandler;

    public long countAll() {
        return userRepository.count();
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> findByReferenceId(Long referenceId, Pageable pageable) {
        return userRepository.findByReferenceId(referenceId, pageable);
    }

    public User getUser(Long userId) {
        return userRepository.findOne(userId);
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public User findUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public long countByCreatedDateBetween(Date startDate, Date endDate) {
        return userRepository.countByCreatedDateBetween(startDate, endDate);
    }


    /**
     * Create new User. Usually it's called after verify phone and code combination and it's new user
     *
     * @param phone is used in username
     * @param userType
     * @param password
     * @param preRegister 如果是预注册，enabled设成false，如果不是，enabled设置成true
     * @return
     */
    @Transactional
    public User createUser(String phone, int userType, String password, boolean preRegister) {
        User user = new User();
        user.setUsername(phone);
        user.setPhone(phone);
        user.setEnabled(!preRegister);
        user.setPreRegister(preRegister);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setPassword(passwordEncoder.encode(password));

        //set user role as default role
        Set<Role> roles = new HashSet<>();

        Long roleID = Constants.USER_ROLE_STUDENT_ID;

        roles.add(roleRepository.findOne(roleID));
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }


    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * After phone and code combination verified successfully,
     * check if it's a new user, a new student or a new teacher.
     * Create accordingly.
     *
     * Institution User should be registered through admin site for now, until open to public.
     * TODO: Note: bind institutionUser's institute property after register.
     *
     *
     * @param phone
     * @param userType
     * @param password
     * @param city 注册时的所在城市
     * @return
     */
    @Transactional
    public User processRegister(String phone, int userType, String password, City city, String nickname) {
        return register(phone, userType, password, city, false,nickname);

    }

    /**
     * Internal actual register.
     * If user is not register by himself (such as getting Coupon),
     * mark him as preRegister and enabled false to prevent login
     *
     * @param phone
     * @param userType
     * @param password
     * @param city
     * @param preRegister if true, mark as preRegister; else, do the real register
     * @return
     */
    protected User register(String phone, int userType, String password, City city, boolean preRegister,String nickname) {
        // Check if User exist. Create one if not.
        User user = getUserByName(phone);
        if (null != user) {
            throw new ServiceException(messageHandler.getMessage("user.register.conflict"));
        }


        //如果用户还不存在，创建新用户，并设置preRegister状态
        if (null == user) {
            // First Time Register, Create User now.
            user = createUser(phone, userType, password, preRegister);
        }

        return user;
    }


    /**
     * Password Reset
     *
     * @param user
     * @param password
     * @return
     */
    public User resetPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    /**
     * Programmatically log user in without password
     *
     * @param user
     */
    public void login(User user) {
        //Check password again
        Assert.isTrue(user.isEnabled() && user.isAccountNonExpired() && user.isAccountNonLocked() && user.isCredentialsNonExpired());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    /**
     * Programmatically log user in
     *
     * @param user
     * @param password
     */
    public void login(User user, String password) {
        //Check password again
        Assert.isTrue(passwordEncoder.matches(password, user.getPassword()), messageHandler.getMessage("prompt.user.login.password.incorrect"));
        Assert.isTrue(user.isEnabled() && user.isAccountNonExpired() && user.isAccountNonLocked() && user.isCredentialsNonExpired());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    //todo
    /**
     * Programmatically log user in
     *
     * @param user
     */
    public void loginWithToken(User user) {
        Assert.isTrue(user.isEnabled() && user.isAccountNonExpired() && user.isAccountNonLocked() && user.isCredentialsNonExpired());
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }


    /**
     * 记录登录事件
     * @param request
     * @param clientId
     * @param deviceToken
     * @param idfa
     * @param deviceTypeParam
     * @param userType
     */
    public void userLogin(HttpServletRequest request, User user, String clientId, String deviceToken, String idfa, String deviceTypeParam, int userType) {
        UserLogin userLogin = new UserLogin();
        userLogin.setLastLoginIp(IPAddressUtil.getClientIP(request));
        userLogin.setClientId(clientId);
        userLogin.setDeviceToken(deviceToken);
        userLogin.setDeviceType(deviceTypeParam);
        userLogin.setUserType(userType);
        userLogin.setUser(user);
        baseRepository.save(userLogin);
    }
}
