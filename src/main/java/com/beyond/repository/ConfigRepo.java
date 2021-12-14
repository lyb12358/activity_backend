package com.beyond.repository;

import com.beyond.pojo.Config;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ConfigRepo extends JpaRepository<Config, Integer> {
}
