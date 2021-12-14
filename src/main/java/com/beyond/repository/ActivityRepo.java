package com.beyond.repository;

import com.beyond.pojo.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ActivityRepo extends JpaRepository<Activity, Integer> {
    Activity findByNameAndUserId(String name, Integer userId);

    Activity findByNameAndUserIdAndIdIsNot(String name, Integer userId, Integer id);
}
