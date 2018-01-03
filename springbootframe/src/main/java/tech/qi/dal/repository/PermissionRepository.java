package tech.qi.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tech.qi.entity.security.Permission;


/**
 * @author wangqi
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findById(long id);

    @Modifying
    @Transactional
    @Query("update Permission p set p.name = ?2 where p.id = ?1")
    int updateName(long id, String newName);

}
