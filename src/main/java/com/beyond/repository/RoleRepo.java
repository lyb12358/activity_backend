package com.beyond.repository;

import com.beyond.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface RoleRepo extends JpaRepository<Role, Integer> {
    List<Role> findByName(String name);

    List<Role> findByIdIsNotAndName(Integer id, String name);
}
