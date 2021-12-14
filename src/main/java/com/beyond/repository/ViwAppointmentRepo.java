package com.beyond.repository;

import com.beyond.pojo.ViwAppointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface ViwAppointmentRepo extends JpaRepository<ViwAppointment, Integer> {
    List<ViwAppointment> findAllByCustomId(Integer id);

    List<ViwAppointment> findAllByActivityId(Integer id, Pageable page);

    List<ViwAppointment> findAllByActivityIdAndCustomId(Integer acId, Integer csId);

    int countByActivityId(Integer id);
}
