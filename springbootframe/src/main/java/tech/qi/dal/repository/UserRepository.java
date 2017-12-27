package tech.qi.dal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.qi.entity.User;
import java.util.Date;



/**
 * @author wangqi
 */
public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User>, UserDetailsService {
    public User findByUsername(String name);
    public User findByPhone(String phone);
    public Page<User> findByReferenceId(Long referenceId, Pageable pageable);
    public long countByCreatedDateBetween(Date startDate, Date endDate);
}
