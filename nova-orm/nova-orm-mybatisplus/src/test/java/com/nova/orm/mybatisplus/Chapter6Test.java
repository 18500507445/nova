package com.nova.orm.mybatisplus;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nova.common.utils.random.RandomUtils;
import com.nova.orm.mybatisplus.chapter6.DynamicUser;
import com.nova.orm.mybatisplus.chapter6.DynamicUserMapper;
import com.nova.orm.mybatisplus.chapter6.TenantUser;
import com.nova.orm.mybatisplus.chapter6.TenantUserMapper;
import com.nova.orm.mybatisplus.config.TenantContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author: wzh
 * @description: 租户测试
 * @date: 2025/08/15 10:02
 */
@SpringBootTest
public class Chapter6Test {

    static {
        System.setProperty("pagehelper.banner", "false");
    }

    @Autowired
    private TenantUserMapper tenantUserMapper;

    @Autowired
    private DynamicUserMapper dynamicUserMapper;

    //做租户测试
    @Test
    public void tenantIdTest() {
        LambdaQueryWrapper<TenantUser> query = new LambdaQueryWrapper<>();
        List<TenantUser> list = tenantUserMapper.selectList(query);
        System.out.println("json = " + JSONUtil.toJsonStr(list));
    }

    //动态表名测试
    @Test
    public void dynamicInsert() {
        for (int i = 0; i < 10; i++) {

            Long tenantId = 1L;
            TenantContext.setTenant(tenantId);
            DynamicUser dynamicUser = new DynamicUser();
            dynamicUser.setTenantId(tenantId);
            dynamicUser.setName(RandomUtils.randomName());
            dynamicUserMapper.insert(dynamicUser);

            tenantId = 2L;
            TenantContext.setTenant(tenantId);
            DynamicUser dynamicUser2 = new DynamicUser();
            dynamicUser2.setTenantId(tenantId);
            dynamicUser2.setName(RandomUtils.randomName());
            dynamicUserMapper.insert(dynamicUser2);

        }
    }

    @Test
    public void dynamicQuery() {
        Long tenantId = 2L;
        TenantContext.setTenant(tenantId);
        LambdaQueryWrapper<DynamicUser> query = new LambdaQueryWrapper<>();
        query.eq(DynamicUser::getTenantId, tenantId);
        List<DynamicUser> list = dynamicUserMapper.selectList(query);
        System.out.println("json = " + JSONUtil.toJsonStr(list));
    }

}
