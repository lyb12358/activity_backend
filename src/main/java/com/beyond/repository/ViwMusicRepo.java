package com.beyond.repository;

import com.beyond.pojo.ViwMusic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ViwMusicRepo extends JpaRepository<ViwMusic, Integer> {
    ViwMusic findByNameAndUserId(String name, Integer userId);

    ViwMusic findByNameAndUserIdAndIdIsNot(String name, Integer userId, Integer id);
}
