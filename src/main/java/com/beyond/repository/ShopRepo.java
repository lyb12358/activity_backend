package com.beyond.repository;

import com.beyond.pojo.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ShopRepo extends JpaRepository<Shop, Integer> {
    Shop findByNameAndUserId(String name, Integer userId);

    List<Shop> findByUserId(Integer id);

    Shop findByNameAndUserIdAndIdIsNot(String name, Integer userId, Integer id);
}
