package com.beyond.repository;

import com.beyond.pojo.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface RolePermissionRepo extends JpaRepository<RolePermission, Integer> {
    List<RolePermission> findByRoleId(Integer id);

    void deleteByRoleId(Integer id);
}
