package com.nova.login.sa.service;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wzh
 * @description: 自定义权限加载接口实现类
 * @date: 2023/09/28 16:14
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * @param loginId   账号id
     * @param loginType 账号类型 账号体系标识，此处可以暂时忽略，在[多账户认证]章节下会对这个概念做详细的解释。
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<>();
        list.add("10001");
        list.add("user.add");
        list.add("user.update");
        list.add("user.get");
        // list.add("user.delete");
        list.add("art.*");
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("super-admin");
        return list;
    }
}
