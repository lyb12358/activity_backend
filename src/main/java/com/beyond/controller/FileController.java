package com.beyond.controller;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.beyond.pojo.ViwAppointment;
import com.beyond.pojo.form.GeneralSearchForm;
import com.beyond.service.ActivityService;
import com.beyond.service.ShopAndCustomService;
import com.beyond.service.UserService;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.ExcelUtil;
import com.beyond.utils.PojoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author lyb
 * @create 12/14/21 2:04 PM
 */
@Slf4j
@RestController
@ApiIgnore
public class FileController {
    @Autowired
    public ShopAndCustomService scService;
    @Autowired
    public UserService userService;
    @Autowired
    public ActivityService acService;

    //导出预约信息
    @RequestMapping(value = "/excel/appointment", method = RequestMethod.POST)
    public void getVerifiedList(HttpServletResponse response, @RequestBody GeneralSearchForm form) {
        form.setRow(100000);
        form.setPage(1);
        DataGridResult result = scService.getAppointmentList(form);
        List<ViwAppointment> appList = result.getRows();
        List<Map<String, Object>> list = new ArrayList<>();
        List<ExcelExportEntity> beanList = new ArrayList<>();
        for (ViwAppointment app : appList) {
            Map<String, Object> map = new HashMap<>();
            map.put("shopName", PojoUtil.getClassValue(app, "shopName"));
            map.put("activityName", PojoUtil.getClassValue(app, "activityName"));
            map.put("customName", PojoUtil.getClassValue(app, "customName"));
            map.put("mobile", PojoUtil.getClassValue(app, "mobile"));
            map.put("isVerified", PojoUtil.getClassValue(app, "isVerified"));
            map.put("verifiedUserName", PojoUtil.getClassValue(app, "verifiedUserName"));
            map.put("gmtVerified", PojoUtil.getClassValue(app, "gmtVerified"));
            map.put("gmtCreate", PojoUtil.getClassValue(app, "gmtCreate"));
            list.add(map);
        }
        beanList.add(new ExcelExportEntity("门店名称", "shopName"));
        beanList.add(new ExcelExportEntity("活动名称", "activityName"));
        beanList.add(new ExcelExportEntity("客户名称", "customName"));
        beanList.add(new ExcelExportEntity("手机", "mobile"));
        beanList.add(new ExcelExportEntity("是否核销", "isVerified"));
        beanList.add(new ExcelExportEntity("核销人名称", "verifiedUserName"));
        beanList.add(new ExcelExportEntity("核销日期", "gmtVerified"));
        beanList.add(new ExcelExportEntity("预约日期", "gmtCreate"));
        ExcelUtil.exportExcel(list, "预约信息", "预约信息", beanList, "预约信息.xls", response);
    }
}
