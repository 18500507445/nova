package com.nova.tools.demo.guava;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2022/10/13 17:59
 */
public class MapTest {

    @Test
    public void createMap(){
        Map<String, Object> silver = ImmutableMap.of("id", 1, "name", "name");
        System.out.println(JSONUtil.toJsonStr(silver));
    }
}
