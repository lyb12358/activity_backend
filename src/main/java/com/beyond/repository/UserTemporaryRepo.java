package com.beyond.repository;

import com.beyond.pojo.Activity;
import com.beyond.pojo.UserTemporary;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface UserTemporaryRepo extends JpaRepository<UserTemporary, Integer> {
}
