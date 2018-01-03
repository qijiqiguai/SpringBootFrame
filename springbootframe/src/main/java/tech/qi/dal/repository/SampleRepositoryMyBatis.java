package tech.qi.dal.repository;

import tech.qi.entity.security.Permission;

import java.util.List;

/**
 *
 * @author wangqi
 * @date 2018/1/3 下午2:58
 */
public interface SampleRepositoryMyBatis {
    List<Permission> getAllPermission();
}
