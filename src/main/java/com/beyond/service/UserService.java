package com.beyond.service;


import com.beyond.pojo.Permission;
import com.beyond.pojo.Role;
import com.beyond.pojo.User;
import com.beyond.pojo.form.UserSearchForm;
import com.beyond.utils.DataGridResult;
import com.beyond.utils.NormalException;
import com.beyond.utils.SelectOption;

import java.util.List;

public interface UserService {


    // auth
    Boolean login(User user);

    User selectByAccount(String account);

    User getRbac(Integer id);

    // user manage
    // 获取用户表
    DataGridResult getUserList(UserSearchForm form);

    void addUser(User user) throws NormalException;

    User getUserById(Integer id);

    User getUserByMp(String account, String pwd);

    // 修改密码
    void updatePassword(Integer id, String password) throws NormalException;

    // // 重置密码
    // void resetPassword(Integer id);

    // role manage
    // 获取角色表
    DataGridResult getRoleList(UserSearchForm form);

    void addRole(Role role) throws NormalException;

    void updateRole(Role role) throws NormalException;

    List<SelectOption> getRoleOptions();

    // user role
    List<Integer> getUserRole(Integer id);

    void updateUserRole(Integer id, List<Integer> list) throws NormalException;

    // role permission
    List<Integer> getRolePermission(Integer id);

    void updateRolePermission(Integer id, List<Integer> list) throws NormalException;

    // permission
    List<Integer> getStaticPermissionArrayByParent(Integer id);

    List<Permission> getStaticPermissionByParent(Integer id);

}
