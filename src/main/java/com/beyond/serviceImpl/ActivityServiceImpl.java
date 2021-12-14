package com.beyond.serviceImpl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.beyond.pojo.Activity;
import com.beyond.pojo.ActivityVisit;
import com.beyond.pojo.Appointment;
import com.beyond.pojo.Config;
import com.beyond.pojo.Custom;
import com.beyond.pojo.Music;
import com.beyond.pojo.ViwActivity;
import com.beyond.pojo.ViwMusic;
import com.beyond.pojo.form.ActivitySearchForm;
import com.beyond.pojo.form.GeneralSearchForm;
import com.beyond.repository.ActivityRepo;
import com.beyond.repository.ActivityVisitRepo;
import com.beyond.repository.AppointmentRepo;
import com.beyond.repository.ConfigRepo;
import com.beyond.repository.CustomRepo;
import com.beyond.repository.MusicRepo;
import com.beyond.repository.ViwActivityRepo;
import com.beyond.repository.ViwMusicRepo;
import com.beyond.service.ActivityService;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.NormalException;
import com.beyond.utils.SelectOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * @Author lyb
 * @create 11/1/21 12:30 AM
 */
@Slf4j
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityRepo acRepo;
    @Autowired
    private ViwActivityRepo ViwAcRepo;
    @Autowired
    private MusicRepo muRepo;
    @Autowired
    private ViwMusicRepo viwMuRepo;
    @Autowired
    private CustomRepo csRepo;
    @Autowired
    private AppointmentRepo apRepo;
    @Autowired
    private ConfigRepo configRepo;
    @Autowired
    private ActivityVisitRepo acvRepo;


    @Override
    public DataGridResult getMusicList(GeneralSearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "id");
        ViwMusic mu = new ViwMusic();
        ExampleMatcher match = ExampleMatcher.matching();
        if (form.getName() != null && !"".equals(form.getName())) {
            mu.setName(form.getName());
            match = match.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getType() != null && !"".equals(form.getType()) && form.getType() != 1) {
            mu.setUserId(form.getUserId());
        }
        Example<ViwMusic> example = Example.of(mu, match);
        Page<ViwMusic> page = viwMuRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    public DataGridResult getActivityList(ActivitySearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "id");
        ViwActivity ac = new ViwActivity();
        ExampleMatcher match = ExampleMatcher.matching();
        if (form.getName() != null && !"".equals(form.getName())) {
            ac.setName(form.getName());
            match = match.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getType() != null && !"".equals(form.getType()) && form.getType() != 1) {
            ac.setUserId(form.getUserId());
        }
        Example<ViwActivity> example = Example.of(ac, match);
        Page<ViwActivity> page = ViwAcRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    @Transactional
    public void makeAnAppointment(String mobile, Integer userId, Integer activityId) throws NormalException {
        //fixme 预约限制
        List<Appointment> appointmentList = apRepo.findAllByActivityIdAndCustomId(activityId, userId);
        Activity ac = acRepo.getById(activityId);

        if (appointmentList.size() >= ac.getCustomLimit()) {
            throw new NormalException("您已达到本次活动预约次数上限，请联系门店！");
        }
        Custom cs = csRepo.getById(userId);
        cs.setMobile(mobile);
        cs = csRepo.save(cs);
        //插入浏览记录，记录用户预约轨迹type=2
        ActivityVisit av = new ActivityVisit();
        av.setType(2);
        av.setCustomId(userId);
        av.setActivityId(activityId);
        av.setGmtCreate(new Date());
        av.setCustomAvatar(cs.getAvatar());
        av.setCustomName(cs.getName());
        addActivityVisit(av);
        //插入预约记录
        Appointment ap = new Appointment();
        ap.setActivityId(activityId);
        ap.setUserId(ac.getUserId());
        ap.setCustomId(userId);
        ap.setMobile(mobile);
        ap.setCustomName(cs.getName());
        ap.setCustomAvatar(cs.getAvatar());
        //参数1为终端ID
        //参数2为数据中心ID
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        long snowId = snowflake.nextId();
        ap.setCode(String.valueOf(snowId));
        ap.setComment("活动真实有效！");
        apRepo.save(ap);

    }

    @Override
    public List<SelectOption> getAppointmentByCustomId(Integer userId) {
        List<Appointment> listAp = apRepo.findAllByCustomId(userId);
        TreeMap<Integer, Activity> tm = new TreeMap<>();
        Activity ac = new Activity();
        for (Appointment ap : listAp) {
            if (tm.containsKey(ap.getActivityId())) {
                ac = tm.get(ap.getActivityId());
                List<Appointment> apList = ac.getAppointmentList();
                apList.add(ap);
                ac.setAppointmentList(apList);
                tm.replace(ap.getActivityId(), ac);
            } else {
                ac = acRepo.getById(ap.getActivityId());
                List<Appointment> apList = new ArrayList<>();
                apList.add(ap);
                ac.setAppointmentList(apList);
                tm.put(ap.getActivityId(), ac);
            }
        }
        List<SelectOption> soList = new ArrayList<>();
        Iterator<Integer> iterator = tm.keySet().iterator();
        while (iterator.hasNext()) {
            Integer id = iterator.next();
            SelectOption so = new SelectOption();
            so.setValue(id);
            so.setLabel(tm.get(id).getName());
            so.setData(tm.get(id));
            soList.add(so);
        }
        return soList;
    }

    @Override
    public Music getMusicById(Integer id) {
        return muRepo.getById(id);
    }

    @Override
    @Transactional
    public void addMusic(Music music) throws NormalException {

        if (null == music.getId()) {
            Music music1 = muRepo.findByNameAndUserId(music.getName(), music.getUserId());
            if (null != music1) {
                throw new NormalException("名称不允许重复！");
            }
        } else {
            Music music1 = muRepo.findByNameAndUserIdAndIdIsNot(music.getName(), music.getUserId(), music.getId());
            if (null != music1) {
                throw new NormalException("名称不允许重复！");
            }
        }
        try {
            muRepo.save(music);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }

    @Override
    public List<SelectOption> getMusicOptions() {
        List<SelectOption> selectOptions = new ArrayList<>();
        List<ViwMusic> viwMusics = viwMuRepo.findAll();
        for (ViwMusic viwMusic : viwMusics) {
            SelectOption so = new SelectOption();
            so.setValue(viwMusic.getId());
            so.setLabel(viwMusic.getName());
            so.setUrl(viwMusic.getUrl());
            so.setName(viwMusic.getUserName());
            selectOptions.add(so);
        }
        return selectOptions;
    }

    @Override
    public Config getConfigById(Integer id) {
        return configRepo.getById(id);
    }

    @Override
    @Transactional
    public void addConfig(Config config) throws NormalException {
        try {
            configRepo.save(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }

    @Override
    public Activity getActivityById(Integer id) {
        return acRepo.getById(id);
    }

    @Override
    @Transactional
    public void addActivity(Activity ac) throws NormalException {

        if (null == ac.getId()) {
            Activity ac1 = acRepo.findByNameAndUserId(ac.getName(), ac.getUserId());
            if (null != ac1) {
                throw new NormalException("名称不允许重复！");
            }
        } else {
            Activity ac1 = acRepo.findByNameAndUserIdAndIdIsNot(ac.getName(), ac.getUserId(), ac.getId());
            if (null != ac1) {
                throw new NormalException("名称不允许重复！");
            }
        }
        try {
            acRepo.save(ac);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }

    @Override
    @Transactional
    public void updateActivityWithDetailImage(Integer id, String detailImage) throws NormalException {
        try {
            Activity ac = acRepo.getById(id);
            ac.setDetailImage(detailImage);
            acRepo.save(ac);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }

    }

    //count
    @Override
    public int countAppointment(Integer id) {
        return apRepo.countByActivityId(id);
    }

    @Override
    public int countVisit(Integer id) {
        return acvRepo.countByActivityId(id);
    }

    //find by ac id
    @Override
    public List<Object> getVisitListByActivityId(Integer id) {
        //Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable page = PageRequest.of(0, 12);
        return acvRepo.findAllByActivityId(id, page);
    }

    @Override
    public List<Appointment> getAppListByActivityId(Integer id) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable page = PageRequest.of(0, 20, sort);
        return apRepo.findAllByActivityId(id, page);
    }

    @Override
    @Transactional
    public void addActivityVisit(ActivityVisit ac) throws NormalException {
        try {
            acvRepo.save(ac);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }
}
