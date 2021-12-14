package com.beyond.serviceImpl;

import com.beyond.pojo.Permission;
import com.beyond.pojo.Role;
import com.beyond.pojo.RolePermission;
import com.beyond.pojo.User;
import com.beyond.pojo.UserRole;
import com.beyond.pojo.form.UserSearchForm;
import com.beyond.repository.PermissionRepo;
import com.beyond.repository.RolePermissionRepo;
import com.beyond.repository.RoleRepo;
import com.beyond.repository.UserRepo;
import com.beyond.repository.UserRoleRepo;
import com.beyond.service.UserService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRoleRepo userRoleRepo;
    @Autowired
    private RolePermissionRepo rolePermissionRepo;
    @Autowired
    private PermissionRepo permissionRepo;


    @Override
    public Boolean login(User user) {
        String account = user.getAccount();
        String password = user.getPassword();
        String MD5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        User newUser = userRepo.findByAccountAndPassword(account, MD5Password);
        if (null != newUser && newUser.getType() != 1 && newUser.getType() != 2) {
            return false;
        }
        User newUser1 = userRepo.findByAccountAndPasswordAndStatusAndIsDel(account, MD5Password, 1, false);
        return null != newUser1;
    }

    @Override
    public User getUserByMp(String account, String pwd) {
        String MD5Password = DigestUtils.md5DigestAsHex(pwd.getBytes());
        return userRepo.findByAccountAndPassword(account, MD5Password);
    }

    @Override
    public User getRbac(Integer id) {
        User user = userRepo.getById(id);
        user.setPassword("");
        List<UserRole> roleList = userRoleRepo.findByUserId(id);
        List<Integer> permissions = new ArrayList<>();
        LinkedHashSet<Integer> set = new LinkedHashSet<>();
        Integer roleId = null;
        for (UserRole ur : roleList) {
            roleId = ur.getRoleId();
            List<RolePermission> rolePermissionList = rolePermissionRepo.findByRoleId(roleId);
            for (RolePermission rp : rolePermissionList) {
                set.add(rp.getPermissionId());
            }
        }
        permissions.addAll(set);
        user.setPermissions(permissions);
        return user;
    }

    @Override
    public User selectByAccount(String account) {
        User user = userRepo.findByAccount(account);
        return user;
    }

    @Override
    public DataGridResult getUserList(UserSearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "id");
        User user = new User();
        ExampleMatcher match = ExampleMatcher.matching();
        if (form.getAccount() != null && !"".equals(form.getAccount())) {
            user.setAccount(form.getAccount());
            match = match.withMatcher("account", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getName() != null && !"".equals(form.getName())) {
            user.setName(form.getName());
            match = match.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        if (form.getType() != null && !"".equals(form.getType()) && form.getType() != 1) {
            user.setParentId(form.getUserId());
        }
        Example<User> example = Example.of(user, match);
        Page<User> page = userRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    public User getUserById(Integer id) {
        User user = userRepo.getById(id);
        return user;
    }

    @Override
    @Transactional
    public void addUser(User user) throws NormalException {

        if (null == user.getId()) {
            String ps = "111111";
            if (user.getPassword() != null && !"".equals(user.getPassword())) {
                ps = user.getPassword();
            }
            String MD5Password = DigestUtils.md5DigestAsHex(ps.getBytes());
            user.setPassword(MD5Password);
            List<User> list = userRepo.findByNameAndParentId(user.getName(), user.getParentId());
            if (list.size() != 0) {
                throw new NormalException("同一管理员下姓名不允许重复！");
            }
            User user1 = userRepo.findByAccount(user.getAccount());
            if (null != user1) {
                throw new NormalException("全局账号不允许重复！");
            }
        } else {
            List<User> list = userRepo.findByNameAndParentIdAndIdIsNot(user.getName(), user.getParentId(), user.getId());
            if (list.size() != 0) {
                throw new NormalException("同一管理员下姓名不允许重复！");
            }
            User user1 = userRepo.findByAccountAndIdIsNot(user.getAccount(), user.getId());
            if (null != user1) {
                throw new NormalException("全局账号不允许重复！");
            }
        }
        if (user.getType() != 1 && user.getType() != 2 && (null == user.getShopId() || "".equals(user.getShopId()))) {
            throw new NormalException("店员账号必须指定门店！");
        }
        try {
            userRepo.save(user);
            UserRole ur = new UserRole();
            ur.setUserId(user.getId());
            ur.setRoleId(user.getType());
            userRoleRepo.deleteByUserId(user.getId());
            userRoleRepo.save(ur);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("操作失败");
        }
    }

    // 修改密码
    @Override
    public void updatePassword(Integer id, String password) throws NormalException {
        User user = userRepo.getById(id);
        String MD5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(MD5Password);
        try {
            userRepo.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("修改失败");
        }

    }

    // role manage
    // 获取角色表
    @Override
    public DataGridResult getRoleList(UserSearchForm form) {
        Pageable pageable = PageRequest.of(form.getPage() - 1, form.getRow(), Sort.Direction.DESC, "id");
        ExampleMatcher match = ExampleMatcher.matching();
        Role role = new Role();
        if (form.getName() != null && !"".equals(form.getName())) {
            role.setName(form.getName());
            match = match.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        }
        Example<Role> example = Example.of(role, match);
        Page<Role> page = roleRepo.findAll(example, pageable);
        DataGridResult result = new DataGridResult();
        result.setRows(page.getContent());
        result.setTotal(page.getTotalElements());
        return result;
    }

    @Override
    public void addRole(Role role) throws NormalException {
        List<Role> list = roleRepo.findByName(role.getName());
        if (list.size() != 0) {
            throw new NormalException("角色名称不允许重复！");
        }
        try {
            roleRepo.save(role);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("添加失败");
        }
    }

    @Override
    public void updateRole(Role role) throws NormalException {
        List<Role> list = roleRepo.findByIdIsNotAndName(role.getId(), role.getName());
        if (list.size() != 0) {
            throw new NormalException("角色名称不允许重复！");
        }
        try {
            roleRepo.save(role);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("修改失败");
        }
    }

    @Override
    public List<SelectOption> getRoleOptions() {
        List<Role> list = roleRepo.findAll();
        List<SelectOption> options = new ArrayList<>();
        for (Role role : list) {
            SelectOption option = new SelectOption();
            option.setLabel(role.getName() + "(" + role.getRemark() + ")");
            option.setValue(role.getId());
            options.add(option);
        }
        return options;

    }

    // user role
    @Override
    public List<Integer> getUserRole(Integer id) {
        List<UserRole> list = userRoleRepo.findByUserId(id);
        List<Integer> userRoleList = new ArrayList<>();
        for (UserRole userRole : list) {
            userRoleList.add(userRole.getRoleId());
        }
        return userRoleList;
    }

    @Override
    public void updateUserRole(Integer id, List<Integer> list) throws NormalException {
        try {
            userRoleRepo.deleteByUserId(id);
            List<UserRole> userRoleList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(list.get(i));
                userRoleList.add(userRole);
            }
            userRoleRepo.saveAll(userRoleList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("更新失败");
        }
    }

    // role permission
    @Override
    public List<Integer> getRolePermission(Integer id) {
        List<RolePermission> list = rolePermissionRepo.findAll();
        LinkedHashSet<Integer> set = new LinkedHashSet<>(list.size());
        for (RolePermission rolePermission : list) {
            set.add(rolePermission.getPermissionId());
        }
        List<Integer> permissionList = new ArrayList<>();
        permissionList.addAll(set);
        return permissionList;
    }

    @Override
    public void updateRolePermission(Integer id, List<Integer> list) throws NormalException {
        try {
            rolePermissionRepo.deleteByRoleId(id);
            List<RolePermission> permissionList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(id);
                rolePermission.setPermissionId(list.get(i));
                permissionList.add(rolePermission);
            }
            rolePermissionRepo.saveAll(permissionList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NormalException("更新失败");
        }
    }

    // permission
    @Override
    public List<Integer> getStaticPermissionArrayByParent(Integer id) {
        List<Permission> list = permissionRepo.findByParentId(id);
        List<Integer> arraylist = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            arraylist.add(list.get(i).getId());
        }
        return arraylist;
    }

    @Override
    public List<Permission> getStaticPermissionByParent(Integer id) {
        List<Permission> list = permissionRepo.findByParentId(id);
        return list;
    }
}
