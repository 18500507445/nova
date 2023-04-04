package com.nova.tools.java8.collection;

import com.alibaba.fastjson2.JSONObject;
import com.nova.tools.demo.entity.GroupPeople;
import com.nova.tools.demo.entity.Myself;
import com.nova.tools.demo.entity.People;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: Map练习
 * @author: wzh
 * @date: 2023/4/4 22:20
 */
class MapDemo {

    /**
     * Java 8 forEachMap
     */
    @Test
    public void forEachMap() {
        List<People> peopleResult = Myself.EXERCISE_LIST;

        List<GroupPeople> groupList = new ArrayList<>();

        HashMap<Integer, String> map = new HashMap<>(16);

        peopleResult.forEach(people -> {
            map.put(people.getGroupId(), people.getGroupName());
        });

        //Map<Integer, String> map = peopleResult.stream().collect(toMap(People::getGroupId, People::getGroupName));

        Map<Integer, List<People>> group = peopleResult.stream().collect(Collectors.groupingBy(People::getGroupId));
        for (Map.Entry<Integer, List<People>> next : group.entrySet()) {
            GroupPeople groupPeople = new GroupPeople();
            groupPeople.setGroupId(next.getKey());
            groupPeople.setGroupName(map.get(next.getKey()));
            List<People> peopleList = new ArrayList<>();
            next.getValue().forEach(people -> {
                People peopleData = new People();
                peopleData.setId(people.getId());
                peopleData.setAge(people.getAge());
                peopleData.setName(people.getName());
                peopleData.setDescription(people.getDescription());
                peopleList.add(peopleData);

            });
            groupPeople.setListPeople(peopleList);
            groupList.add(groupPeople);
        }

        System.out.println(JSONObject.toJSONString(groupList));
    }
}
