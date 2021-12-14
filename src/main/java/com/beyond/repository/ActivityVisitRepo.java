package com.beyond.repository;

import com.beyond.pojo.ActivityVisit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ActivityVisitRepo extends JpaRepository<ActivityVisit, Integer> {
    int countByActivityId(Integer id);

    //@Query("select  distinct new ActivityVisit(p.c.ustomAvatar)  from  ActivityVisit p where p.activityId=?1  ")
    @Query(value = "select distinct p.custom_avatar as customAvatar  from  (select * from t_activity_visit_lyb order by id desc) p  where p.activity_id=?1", nativeQuery = true)
    List<Object> findAllByActivityId(Integer activityId, Pageable page);
}
