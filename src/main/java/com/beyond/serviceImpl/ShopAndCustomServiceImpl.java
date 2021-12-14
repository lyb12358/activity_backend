package com.beyond.serviceImpl;

import com.beyond.pojo.Appointment;
import com.beyond.pojo.Custom;
import com.beyond.pojo.Shop;
import com.beyond.pojo.ViwAppointment;
import com.beyond.pojo.ViwShop;
import com.beyond.pojo.form.GeneralSearchForm;
import com.beyond.repository.AppointmentRepo;
import com.beyond.repository.CustomRepo;
import com.beyond.repository.ShopRepo;
import com.beyond.repository.ViwAppointmentRepo;
import com.beyond.repository.ViwShopRepo;
import com.beyond.service.ShopAndCustomService;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.NormalException;
import com.beyond.utils.SelectOption;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
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
import java.util.List;

/**
 * @Author lyb
 * @create 11/1/21 12:30 AM
 */
@Slf4j
@Service
public class ShopAndCustomServiceImpl implements ShopAndCustomService {
    @Autowired
    private CustomRepo csRepo;
    @Autowired
    private ShopRepo shopRepo;
    @Autowired
    private ViwShopRepo viwShopRepo;
    @Autowired
    private AppointmentRepo apRepo;
    @Autowired
    private ViwAppointmentRepo vapRepo;

    @Override
    public Custom getCustomByWechat(WxOAuth2UserInfo wxUser) {
        Custom cs = csRepo.findByCredential(wxUser.getOpenid());
        if (cs != null) {
            cs.setName(wxUser.getNickname());
            cs.setAvatar(wxUser.getHeadImgUrl());
            return csRepo.save(cs);
        } else {
            Custom cs1 = new Custom();
            cs1.setName(wxUser.getNickname());
            cs1.setAvatar(wxUser.getHeadImgUrl());
            cs1.setCredential(wxUser.getOpenid());
            return csRepo.save(cs1);
        }
    }

    @Override
    public DataGridResult getShopList(GeneralSearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "id");
        ViwShop shop = new ViwShop();
        ExampleMatcher match = ExampleMatcher.matching();
        if (form.getName() != null && !"".equals(form.getName())) {
            shop.setName(form.getName());
            match = match.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getType() != null && !"".equals(form.getType()) && form.getType() != 1) {
            shop.setUserId(form.getUserId());
        }
        shop.setIsDel(false);
        Example<ViwShop> example = Example.of(shop, match);
        Page<ViwShop> page = viwShopRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    public Shop getShopById(Integer id) {
        return shopRepo.getById(id);
    }

    @Override
    @Transactional
    public void addShop(Shop shop) throws NormalException {

        if (null == shop.getId()) {
            Shop shop1 = shopRepo.findByNameAndUserId(shop.getName(), shop.getUserId());
            if (null != shop1) {
                throw new NormalException("名称不允许重复！");
            }
        } else {
            Shop shop1 = shopRepo.findByNameAndUserIdAndIdIsNot(shop.getName(), shop.getUserId(), shop.getId());
            if (null != shop1) {
                throw new NormalException("名称不允许重复！");
            }
        }
        try {
            shopRepo.save(shop);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }

    @Override
    public List<SelectOption> getShopOptions(Integer type, Integer id) {
        List<SelectOption> selectOptions = new ArrayList<>();
        List<ViwShop> viwShops;
        if (type == 1) {
            viwShops = viwShopRepo.findAll();
        } else {
            viwShops = viwShopRepo.findByUserId(id);
        }
        for (ViwShop viwShop : viwShops) {
            SelectOption so = new SelectOption();
            so.setValue(viwShop.getId());
            so.setLabel(viwShop.getName());
            so.setName(viwShop.getUserName());
            selectOptions.add(so);
        }
        return selectOptions;
    }

    @Override
    public Custom getCustomById(Integer id) {
        return csRepo.getById(id);
    }

    @Override
    @Transactional
    public void addCustom(Custom cs) throws NormalException {
        try {
            csRepo.save(cs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }

    @Override
    public DataGridResult getCustomList(GeneralSearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "id");
        Custom cs = new Custom();
        ExampleMatcher match = ExampleMatcher.matching();
        if (form.getName() != null && !"".equals(form.getName())) {
            cs.setName(form.getName());
            match = match.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        Example<Custom> example = Example.of(cs, match);
        Page<Custom> page = csRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    //
    @Override
    public DataGridResult getAppointmentList(GeneralSearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "id");
        ViwAppointment va = new ViwAppointment();
        ExampleMatcher match = ExampleMatcher.matching();
        if (form.getType() != null && !"".equals(form.getType()) && form.getType() != 1) {
            va.setUserId(form.getUserId());
        }
        if (form.getName() != null && !"".equals(form.getName())) {
            va.setCustomName(form.getName());
            match = match.withMatcher("customName", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getShopName() != null && !"".equals(form.getShopName())) {
            va.setShopName(form.getShopName());
            match = match.withMatcher("shopName", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getActivityName() != null && !"".equals(form.getActivityName())) {
            va.setActivityName(form.getActivityName());
            match = match.withMatcher("activityName", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getMobile() != null && !"".equals(form.getMobile())) {
            va.setMobile(form.getMobile());
            match = match.withMatcher("mobile", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        Example<ViwAppointment> example = Example.of(va, match);
        Page<ViwAppointment> page = vapRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    public DataGridResult getVerifiedList(GeneralSearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "gmtVerified");
        ViwAppointment va = new ViwAppointment();
        ExampleMatcher match = ExampleMatcher.matching();
        if (form.getUserId() != null && !"".equals(form.getUserId())) {
            va.setVerifiedUser(form.getUserId());
        }
        if (form.getShopName() != null && !"".equals(form.getShopName())) {
            va.setShopName(form.getShopName());
            match = match.withMatcher("shopName", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getActivityName() != null && !"".equals(form.getActivityName())) {
            va.setActivityName(form.getActivityName());
            match = match.withMatcher("activityName", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getMobile() != null && !"".equals(form.getMobile())) {
            va.setMobile(form.getMobile());
            match = match.withMatcher("mobile", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        Example<ViwAppointment> example = Example.of(va, match);
        Page<ViwAppointment> page = vapRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    public Appointment getAppointmentById(Integer id) {
        return apRepo.getById(id);
    }

    @Override
    public Appointment getAppointmentByCode(String code) {
        return apRepo.findByCode(code);
    }

    @Override
    public void verifiedCodeById(String code, Integer userId) {
        Appointment ap = getAppointmentByCode(code);
        ap.setIsVerified(true);
        ap.setVerifiedUser(userId);
        ap.setGmtVerified(new Date());
        try {
            apRepo.save(ap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }
}
