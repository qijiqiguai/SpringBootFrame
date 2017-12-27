package tech.qi.dal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.qi.entity.User;



/**
 * @author wangqi
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * findByUsername
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * findByPhone
     * @param phone
     * @return
     */
    User findByPhone(String phone);

}
