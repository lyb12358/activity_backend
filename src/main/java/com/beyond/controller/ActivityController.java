package com.beyond.controller;

import com.beyond.pojo.Activity;
import com.beyond.pojo.ActivityVisit;
import com.beyond.pojo.Appointment;
import com.beyond.pojo.AppointmentProd;
import com.beyond.pojo.Config;
import com.beyond.pojo.Custom;
import com.beyond.pojo.Music;
import com.beyond.pojo.Product;
import com.beyond.pojo.Shop;
import com.beyond.pojo.form.ActivitySearchForm;
import com.beyond.pojo.form.GeneralSearchForm;
import com.beyond.service.ActivityService;
import com.beyond.service.ProductService;
import com.beyond.service.ShopAndCustomService;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.ResponseBean;
import com.beyond.utils.SelectOption;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.List;

/**
 * @Author lyb
 * @create 10/29/21 9:11 AM
 */
@Slf4j
@Api(tags = "活动预约模块")
@RestController
public class ActivityController {
    @Autowired
    public ActivityService acService;
    @Autowired
    public ShopAndCustomService scService;
    @Autowired
    public ProductService prodService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "activityId", value = "活动id", required = true),
            @ApiImplicitParam(name = "userId", value = "客户id", required = true)})
    @ApiOperation(value = "根据活动id获取活动详情（微信专用）")
    @RequestMapping(value = "/activity", method = RequestMethod.GET)
    public ResponseBean<Activity> getActivity(Integer activityId, Integer userId) {
        Activity ac = acService.getActivityById(activityId);
        if (null != ac.getMusicId() && !"".equals(ac.getMusicId())) {
            Music mu = acService.getMusicById(ac.getMusicId());
            ac.setMusic(mu.getUrl());
        }
        Shop shop = scService.getShopById(ac.getShopId());
        ac.setShopName(shop.getName());
        ac.setShopAddr(shop.getAddress());
        ac.setShopContact(shop.getContact());
        //浏览人数应该大于虚拟预约人数的2.5倍
        //fakeCustom必须小于limit
        //虚拟预约加真实预约
        //Integer trueCount = acService.countVisit(ac.getId());
        ac.setCount(ac.getFakeCount() + acService.countVisit(ac.getId()));

        ac.setTotalCustom(ac.getFakeAppointment() + acService.countAppointment(ac.getId()));

        List<Appointment> app = acService.getAppListByActivityId(ac.getId());
        ac.setAppointmentList(app);
        List<Object> cs = acService.getVisitListByActivityId(ac.getId());
        ac.setCustomList(cs);
        //插入客户浏览记录
        Custom custom = scService.getCustomById(userId);
        ActivityVisit av = new ActivityVisit();
        av.setActivityId(ac.getId());
        av.setCustomId(custom.getId());
        av.setCustomName(custom.getName());
        av.setCustomAvatar(custom.getAvatar());
        av.setType(1);
        av.setGmtCreate(new Date());
        av.setGmtModified(new Date());
        acService.addActivityVisit(av);
        return ResponseBean.<Activity>builder()
                .code(20000)
                .msg("success")
                .data(ac)
                .build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true),
            @ApiImplicitParam(name = "userId", value = "客户id", required = true),
            @ApiImplicitParam(name = "activityId", value = "活动id", required = true)})
    @ApiOperation(value = "客户预约")
    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ResponseBean<T> makeAnAppointment(String mobile, Integer userId, Integer activityId) {
        acService.makeAnAppointment(mobile, userId, activityId);
        return ResponseBean.<T>builder()
                .code(20000)
                .msg("success")
                .build();
    }

    @ApiImplicitParam(name = "userId", value = "客户id", required = true)
    @ApiOperation(value = "获取客户预约信息")
    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ResponseBean<List<SelectOption>> getAppointmentByCustomId(Integer userId) {
        List<SelectOption> soList = acService.getAppointmentByCustomId(userId);
        return ResponseBean.<List<SelectOption>>builder()
                .code(20000)
                .msg("success")
                .data(soList)
                .build();
    }

    @ApiIgnore
    @RequestMapping(value = "/maintain/musics", method = RequestMethod.POST)
    public ResponseBean getMusicList(@RequestBody GeneralSearchForm form) {
        DataGridResult result = acService.getMusicList(form);
        return new ResponseBean(20000, "success", result);
    }

    @ApiIgnore
    @RequestMapping(value = "/activities", method = RequestMethod.POST)
    public ResponseBean getActivityList(@RequestBody ActivitySearchForm form) {
        DataGridResult result = acService.getActivityList(form);
        return new ResponseBean(20000, "success", result);
    }

    // 获取单个音乐
    @ApiIgnore
    @RequestMapping(value = "/maintain/music/{id}", method = RequestMethod.GET)
    public ResponseBean getMusicById(@PathVariable Integer id) {
        Music music = acService.getMusicById(id);
        return new ResponseBean(20000, "操作成功", music);
    }

    // 添加/修改音乐
    @ApiIgnore
    @RequestMapping(value = "/maintain/music", method = RequestMethod.POST)
    public ResponseBean addMusic(@RequestBody Music music) {
        acService.addMusic(music);
        return new ResponseBean(20000, "操作成功", null);
    }

    @ApiIgnore
    @RequestMapping(value = "/maintain/music/options", method = RequestMethod.GET)
    public ResponseBean getMusicOptions() {
        List<SelectOption> selectOptions = acService.getMusicOptions();
        return new ResponseBean(20000, "操作成功", selectOptions);
    }

    // 获取单个活动
    @ApiIgnore
    @RequestMapping(value = "/activity/{id}", method = RequestMethod.GET)
    public ResponseBean getActivityById(@PathVariable Integer id) {
        Activity activity = acService.getActivityById(id);
        return new ResponseBean(20000, "操作成功", activity);
    }

    // 添加/修改活动
    @ApiIgnore
    @RequestMapping(value = "/activity", method = RequestMethod.POST)
    public ResponseBean addActivity(@RequestBody Activity activity) {
        acService.addActivity(activity);
        return new ResponseBean(20000, "操作成功", null);
    }

    // 获取单个配置
    @ApiIgnore
    @RequestMapping(value = "/config/{id}", method = RequestMethod.GET)
    public ResponseBean getConfigById(@PathVariable Integer id) {
        Config config = acService.getConfigById(id);
        return new ResponseBean(20000, "操作成功", config);
    }

    // 添加/修改配置
    @ApiIgnore
    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public ResponseBean addConfig(@RequestBody Config config) {
        acService.addConfig(config);
        return new ResponseBean(20000, "操作成功", null);
    }

    // 更新详情图
    @ApiIgnore
    @RequestMapping(value = "/activity/detailImage", method = RequestMethod.POST)
    public ResponseBean updateDetailImage(Integer id, String detailImage) {
        acService.updateActivityWithDetailImage(id, detailImage);
        return new ResponseBean(20000, "操作成功", null);
    }

    // 获取活动商品
    @ApiIgnore
    @RequestMapping(value = "/activity/product", method = RequestMethod.GET)
    public ResponseBean getActivityProductList(Integer id) {
        List<Product> list = prodService.getActivityProductList(id);
        return new ResponseBean(20000, "操作成功", list);
    }

    // 更新活动商品
    @ApiIgnore
    @RequestMapping(value = "/activity/product", method = RequestMethod.POST)
    public ResponseBean addActivityProduct(@RequestBody List<Product> list) {
        prodService.addActivityProduct(list);
        return new ResponseBean(20000, "操作成功", null);
    }

    // 更新核销活动商品
    @ApiIgnore
    @RequestMapping(value = "/activity/verify/product", method = RequestMethod.POST)
    public ResponseBean addActivityVerifyProduct(@RequestBody List<AppointmentProd> list) {
        prodService.addActivityVerifyProduct(list);
        return new ResponseBean(20000, "操作成功", null);
    }


}
