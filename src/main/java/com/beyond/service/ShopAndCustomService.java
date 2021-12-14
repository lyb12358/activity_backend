package com.beyond.service;


import com.beyond.pojo.Appointment;
import com.beyond.pojo.Custom;
import com.beyond.pojo.Shop;
import com.beyond.pojo.form.GeneralSearchForm;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.NormalException;
import com.beyond.utils.SelectOption;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.List;

public interface ShopAndCustomService {


    Custom getCustomByWechat(WxOAuth2UserInfo wxUser);

    DataGridResult getCustomList(GeneralSearchForm form);

    DataGridResult getShopList(GeneralSearchForm form);

    void addCustom(Custom cs) throws NormalException;

    Custom getCustomById(Integer id);

    void addShop(Shop shop) throws NormalException;

    Shop getShopById(Integer id);

    List<SelectOption> getShopOptions(Integer type, Integer id);

    //appointment
    DataGridResult getAppointmentList(GeneralSearchForm form);

    //appointment
    DataGridResult getVerifiedList(GeneralSearchForm form);

    Appointment getAppointmentById(Integer id);

    Appointment getAppointmentByCode(String code);

    void verifiedCodeById(String code, Integer userId);
}
