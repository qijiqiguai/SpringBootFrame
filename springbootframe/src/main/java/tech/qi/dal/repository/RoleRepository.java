package tech.qi.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.qi.entity.security.Role;


/**
 * @author wangqi
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
