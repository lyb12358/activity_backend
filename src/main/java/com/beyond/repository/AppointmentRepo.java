package com.beyond.repository;

import com.beyond.pojo.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lyb
 * @create 2019-04-08 16:19
 */

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {
    List<Appointment> findAllByCustomId(Integer id);

    List<Appointment> findAllByActivityId(Integer id, Pageable page);

    List<Appointment> findAllByActivityIdAndCustomId(Integer acId, Integer csId);

    int countByActivityId(Integer id);

    Appointment findByCode(String code);
}
