package tech.qi.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tech.qi.dal.repository.UserRepository;
import tech.qi.entity.User;



/**
 * @author wangqi
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user;
        if (null==username || "".equals(username.trim()) || (user = userRepository.findByUsername(username)) == null) {
            throw new UsernameNotFoundException("No user with username " + username);
        }
        return user;
    }
}
