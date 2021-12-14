package com.beyond.repository;

import com.beyond.pojo.Music;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface MusicRepo extends JpaRepository<Music, Integer> {
    Music findByNameAndUserId(String name, Integer userId);

    Music findByNameAndUserIdAndIdIsNot(String name, Integer userId, Integer id);
}
