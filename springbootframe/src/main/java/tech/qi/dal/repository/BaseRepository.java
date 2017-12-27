package tech.qi.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.qi.entity.support.AbstractAuditableEntity;

/**
 * @author wangqi
 */
public interface BaseRepository extends JpaRepository<AbstractAuditableEntity,Long> {

}
