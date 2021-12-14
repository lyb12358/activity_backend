package com.beyond.repository;

import com.beyond.pojo.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface UserRoleRepo extends JpaRepository<UserRole, Integer> {
    List<UserRole> findByUserId(Integer id);

    void deleteByUserId(Integer id);


}
