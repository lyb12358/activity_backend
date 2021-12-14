package com.beyond.repository;

import com.beyond.pojo.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface PermissionRepo extends JpaRepository<Permission, Integer> {
    List<Permission> findByParentId(Integer id);
}
