package com.beyond.repository;

import com.beyond.pojo.ViwActivity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ViwActivityRepo extends JpaRepository<ViwActivity, Integer> {
    ViwActivity findByNameAndUserId(String name, Integer userId);

    ViwActivity findByNameAndUserIdAndIdIsNot(String name, Integer userId, Integer id);
}
