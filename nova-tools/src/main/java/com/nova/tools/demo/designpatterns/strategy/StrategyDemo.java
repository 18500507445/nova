package com.nova.tools.demo.designpatterns.strategy;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.nova.tools.demo.entity.Myself;
import com.nova.tools.demo.entity.People;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 策略模式Demo
 * @Author: wangzehui
 * @Date: 2021/10/16 11:49 上午
 */
public class StrategyDemo {

    public static void main(String[] args) {
        List<People> list = Myself.PEOPLE_LIST;

        List<People> result = filterPeople(list, new FilterByAge());

        System.out.println(JSONObject.toJSONString(result));

    }

    public static List<People> filterPeople(List<People> list, MyPredicate<People> mp) {
        List<People> result = new ArrayList<>();
        if (CollUtil.isNotEmpty(list)) {
            for (People people : list) {
                if (mp.filter(people)) {
                    result.add(people);
                }
            }
        }
        return result;
    }


}
