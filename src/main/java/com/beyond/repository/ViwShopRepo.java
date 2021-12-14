package com.beyond.repository;

import com.beyond.pojo.ViwShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ViwShopRepo extends JpaRepository<ViwShop, Integer> {
    ViwShop findByNameAndUserId(String name, Integer userId);

    List<ViwShop> findByUserId(Integer userId);

    ViwShop findByNameAndUserIdAndIdIsNot(String name, Integer userId, Integer id);
}
