package com.beyond.controller;

import com.beyond.pojo.Role;
import com.beyond.pojo.User;
import com.beyond.pojo.form.UserSearchForm;
import com.beyond.service.UserService;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.JWTUtil;
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
public class UserController {


    @Autowired
    public UserService userService;

    // auth
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseBean login(@RequestBody User user) {

        if (userService.login(user)) {
            User userDetail = userService.selectByAccount(user.getAccount());
            return new ResponseBean(20000, "登录成功",
                    JWTUtil.sign(userDetail.getId(), user.getAccount(), userDetail.getName()));

        } else {
            return new ResponseBean(40000, "该用户不存在或密码错误或没有登录权限！", null);
        }
    }

    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public ResponseBean getInfoByToken(String token) {
        Integer id = JWTUtil.getId(token);
        User user = userService.getRbac(id);

        return new ResponseBean(20000, "success", user);

    }


    // user manage
    // 获取用户表
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseBean getUserList(@RequestBody UserSearchForm form) {
        DataGridResult result = userService.getUserList(form);
        return new ResponseBean(20000, "success", result);
    }

    // 修改密码
    @RequestMapping(value = "/user/{id}/newPassword/{password}", method = RequestMethod.GET)
    public ResponseBean updatePassword(@PathVariable Integer id, @PathVariable String password) {
        userService.updatePassword(id, password);

        return new ResponseBean(20000, "修改成功", null);

    }

    // 获取用户
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseBean getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return new ResponseBean(20000, "操作成功", user);
    }

    // 添加/修改用户
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseBean addUser(@RequestBody User user) {
        userService.addUser(user);
        return new ResponseBean(20000, "操作成功", null);
    }

    // role manage
    // 获取角色表
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public ResponseBean getRoleList(@RequestBody UserSearchForm form) {
        DataGridResult result = userService.getRoleList(form);
        return new ResponseBean(20000, "success", result);
    }

    // 添加角色
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public ResponseBean addRole(@RequestBody Role role) {
        userService.addRole(role);
        return new ResponseBean(20000, "添加角色成功", null);
    }

    // 更新角色
    @RequestMapping(value = "/role", method = RequestMethod.PUT)
    public ResponseBean updateRole(@RequestBody Role role) {

        userService.updateRole(role);
        return new ResponseBean(20000, "修改角色成功", null);
    }

    // 获取角色options
    @RequestMapping(value = "/roles/options", method = RequestMethod.GET)
    public ResponseBean getRoleOptions() {

        List<SelectOption> result = userService.getRoleOptions();
        return new ResponseBean(20000, "success", result);
    }

    // user role
    // 获取用户拥有的角色
    @RequestMapping(value = "/userRole/{id}", method = RequestMethod.GET)
    public ResponseBean getUserRole(@PathVariable Integer id) {

        List<Integer> result = userService.getUserRole(id);
        return new ResponseBean(20000, "success", result);
    }

    // 批量更新用户拥有的角色
    @RequestMapping(value = "/userRole/{id}", method = RequestMethod.POST)
    public ResponseBean updateUserRole(@PathVariable Integer id, @RequestBody List<Integer> list) {

        userService.updateUserRole(id, list);
        return new ResponseBean(20000, "更新成功", null);
    }

    // role permission
    // 获取角色拥有的权限
    @RequestMapping(value = "/rolePermission/{id}", method = RequestMethod.GET)
    public ResponseBean getRolePermission(@PathVariable Integer id) {

        List<Integer> result = userService.getRolePermission(id);
        return new ResponseBean(20000, "success", result);
    }

    // 批量更新角色拥有的权限
    @RequestMapping(value = "/rolePermission/{id}", method = RequestMethod.POST)
    public ResponseBean updateRolePermission(@PathVariable Integer id, @RequestBody List<Integer> list) {

        userService.updateRolePermission(id, list);
        return new ResponseBean(20000, "更新成功", null);
    }

    // permission
    // 获取静态子权限数组
    @RequestMapping(value = "/permissions/{id}", method = RequestMethod.GET)
    public ResponseBean getStaticPermissionArrayByParent(@PathVariable Integer id) {

        List<Integer> result = userService.getStaticPermissionArrayByParent(id);
        return new ResponseBean(20000, "success", result);
    }


}
