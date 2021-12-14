package com.beyond.repository;

import com.beyond.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByActivityIdAndIsDel(Integer id, Boolean isDel);
}
