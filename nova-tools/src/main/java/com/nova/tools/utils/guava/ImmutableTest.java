package com.nova.tools.utils.guava;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @Description: 不可变集合
 * @Author: wangzehui
 * @Date: 2022/10/13 17:59
 */
public class ImmutableTest {

    @Test
    public void createMap(){
        Map<String, Object> silver = ImmutableMap.of("id", 1, "name", "name");
        System.out.println(JSONUtil.toJsonStr(silver));
    }
}
