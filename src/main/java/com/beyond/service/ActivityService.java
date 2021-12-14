package com.beyond.service;


import com.beyond.pojo.Activity;
import com.beyond.pojo.ActivityVisit;
import com.beyond.pojo.Appointment;
import com.beyond.pojo.Config;
import com.beyond.pojo.Music;
import com.beyond.pojo.form.ActivitySearchForm;
import com.beyond.pojo.form.GeneralSearchForm;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.NormalException;
import com.beyond.utils.SelectOption;

import java.util.List;

public interface ActivityService {


    DataGridResult getActivityList(ActivitySearchForm form);


    void makeAnAppointment(String mobile, Integer userId, Integer activityId);

    List<SelectOption> getAppointmentByCustomId(Integer userId);

    void addActivity(Activity ac) throws NormalException;

    Activity getActivityById(Integer id);

    void updateActivityWithDetailImage(Integer id, String detailImage);

    //music
    DataGridResult getMusicList(GeneralSearchForm form);

    void addMusic(Music music) throws NormalException;

    Music getMusicById(Integer id);

    List<SelectOption> getMusicOptions();

    //config
    void addConfig(Config config) throws NormalException;

    Config getConfigById(Integer id);

    //count
    int countVisit(Integer id);

    int countAppointment(Integer id);

    //find by ac id
    List<Object> getVisitListByActivityId(Integer id);

    List<Appointment> getAppListByActivityId(Integer id);

    void addActivityVisit(ActivityVisit av) throws NormalException;


}
