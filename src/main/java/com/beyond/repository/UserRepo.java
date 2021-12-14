package com.beyond.repository;

import com.beyond.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface UserRepo extends JpaRepository<User, Integer> {
    List<User> findByName(String name);

    List<User> findByNameAndParentId(String name, Integer parentId);

    User findByAccount(String account);

    List<User> findByNameAndIdIsNot(String name, Integer id);

    List<User> findByNameAndParentIdAndIdIsNot(String name, Integer parentId, Integer id);

    User findByAccountAndIdIsNot(String account, Integer id);

    User findByAccountAndPassword(String account, String password);

    User findByAccountAndPasswordAndStatusAndIsDel(String account, String password, Integer status, Boolean isDel);

}
