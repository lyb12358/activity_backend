package com.beyond.controller;

/**
 * @Author lyb
 * @create 10/31/21 11:22 PM
 */

import com.beyond.pojo.Activity;
import com.beyond.pojo.Appointment;
import com.beyond.pojo.Custom;
import com.beyond.pojo.Shop;
import com.beyond.pojo.User;
import com.beyond.pojo.form.GeneralSearchForm;
import com.beyond.service.ActivityService;
import com.beyond.service.ShopAndCustomService;
import com.beyond.service.UserService;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.ResponseBean;
import com.beyond.utils.SelectOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RestController
@ApiIgnore
public class ShopAndCustomController {
    @Autowired
    public ShopAndCustomService scService;
    @Autowired
    public UserService userService;
    @Autowired
    public ActivityService acService;

    @RequestMapping(value = "/shops", method = RequestMethod.POST)
    public ResponseBean getShopList(@RequestBody GeneralSearchForm form) {
        DataGridResult result = scService.getShopList(form);
        return new ResponseBean(20000, "success", result);
    }

    @RequestMapping(value = "/customs", method = RequestMethod.POST)
    public ResponseBean getCustomList(@RequestBody GeneralSearchForm form) {
        DataGridResult result = scService.getCustomList(form);
        return new ResponseBean(20000, "success", result);
    }

    // 获取单个门店
    @RequestMapping(value = "/shop/{id}", method = RequestMethod.GET)
    public ResponseBean getShopById(@PathVariable Integer id) {
        Shop shop = scService.getShopById(id);
        return new ResponseBean(20000, "操作成功", shop);
    }

    // 添加/修改门店
    @RequestMapping(value = "/shop", method = RequestMethod.POST)
    public ResponseBean addShop(@RequestBody Shop shop) {
        scService.addShop(shop);
        return new ResponseBean(20000, "操作成功", null);
    }

    @RequestMapping(value = "/shop/options", method = RequestMethod.GET)
    public ResponseBean getShopOptions(Integer type, Integer id) {
        List<SelectOption> selectOptions = scService.getShopOptions(type, id);
        return new ResponseBean(20000, "操作成功", selectOptions);
    }


    // 获取单个客户
    @RequestMapping(value = "/custom/{id}", method = RequestMethod.GET)
    public ResponseBean getCustomById(@PathVariable Integer id) {
        Custom custom = scService.getCustomById(id);
        return new ResponseBean(20000, "操作成功", custom);
    }

    // 添加/修改客户
    @RequestMapping(value = "/custom", method = RequestMethod.POST)
    public ResponseBean addCustom(@RequestBody Custom custom) {
        scService.addCustom(custom);
        return new ResponseBean(20000, "操作成功", null);
    }

    //预约列表
    @RequestMapping(value = "/appointments", method = RequestMethod.POST)
    public ResponseBean getAppointmentList(@RequestBody GeneralSearchForm form) {
        DataGridResult result = scService.getAppointmentList(form);
        return new ResponseBean(20000, "success", result);
    }

    //是否可以核销？
    @RequestMapping(value = "/appointment/verify/status", method = RequestMethod.GET)
    public ResponseBean checkVerifiedCode(String code, Integer userId, Integer parentId) {
        Appointment app = scService.getAppointmentByCode(code);
        User user = userService.getUserById(userId);
        Activity ac = acService.getActivityById(app.getActivityId());
        if (parentId != 0 && user.getType() != 1) {
            if (app.getUserId() != parentId && user.getType() == 3 && ac.getShopId() != user.getShopId()) {
                return new ResponseBean(30000, "无法核销非本门店关联的活动二维码", null);
            }
            if (app.getUserId() != user.getId() && user.getType() == 2) {
                return new ResponseBean(30000, "无法核销他人创建的活动二维码", null);
            }
        }
        if (null == app) {
            return new ResponseBean(30000, "此码无效！", null);
        }
        if (app.getIsVerified()) {
            return new ResponseBean(30000, "该码已被核销,无法再次核销！", null);
        }
        return new ResponseBean(20000, "该码可核销", app);
    }

    //核销
    @RequestMapping(value = "/appointment/verify", method = RequestMethod.GET)
    public ResponseBean verifiedCodeById(String code, Integer userId, Integer parentId) {
        Appointment app = scService.getAppointmentByCode(code);
        User user = userService.getUserById(userId);
        Activity ac = acService.getActivityById(app.getActivityId());
        if (parentId != 0 && user.getType() != 1) {
            if (user.getType() == 3 && ac.getShopId() != user.getShopId()) {
                return new ResponseBean(30000, "无法核销非本门店关联的活动二维码", null);
            }
            if (app.getUserId() != user.getId() && user.getType() == 2) {
                return new ResponseBean(30000, "无法核销他人创建的活动二维码", null);
            }
        }
        if (null == app) {
            return new ResponseBean(30000, "此码无效！", null);
        }
        if (app.getIsVerified()) {
            return new ResponseBean(30000, "该码已被核销,无法再次核销！", null);
        }
        scService.verifiedCodeById(code, userId);
        return new ResponseBean(20000, "核销成功", null);
    }

    //预约列表
    @RequestMapping(value = "/verifiedAppointments", method = RequestMethod.POST)
    public ResponseBean getVerifiedList(@RequestBody GeneralSearchForm form) {
        DataGridResult result = scService.getVerifiedList(form);
        return new ResponseBean(20000, "success", result);
    }

}
