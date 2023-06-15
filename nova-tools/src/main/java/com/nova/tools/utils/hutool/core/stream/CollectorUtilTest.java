package com.nova.tools.utils.hutool.core.stream;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.stream.CollectorUtil;
import cn.hutool.core.stream.StreamUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectorUtilTest {
    @Test
    public void reduceListMapTest() {
        final Set<Map<String, Integer>> nameScoreMapList = StreamUtil.of(
                // 集合内的第一个map，包含两个key value
                MapUtil.builder("苏格拉底", 1).put("特拉叙马霍斯", 3).build(),
                MapUtil.of("苏格拉底", 2),
                MapUtil.of("特拉叙马霍斯", 1),
                MapUtil.of("特拉叙马霍斯", 2)
        ).collect(Collectors.toSet());
        // 执行聚合
        final Map<String, List<Integer>> nameScoresMap = nameScoreMapList.stream().collect(CollectorUtil.reduceListMap());

        Assert.equals(MapUtil.builder("苏格拉底", Arrays.asList(1, 2))
                        .put("特拉叙马霍斯", Arrays.asList(3, 1, 2)).build(),
                nameScoresMap);
    }

    @Test
    public void testGroupingByAfterValueMapped() {
        List<Integer> list = Arrays.asList(1, 1, 2, 2, 3, 4);
        Map<Boolean, Set<String>> map = list.stream()
                .collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf, LinkedHashSet::new, LinkedHashMap::new));

        Assert.equals(LinkedHashMap.class, map.getClass());
        Assert.equals(new LinkedHashSet<>(Arrays.asList("2", "4")), map.get(Boolean.TRUE));
        Assert.equals(new LinkedHashSet<>(Arrays.asList("1", "3")), map.get(Boolean.FALSE));

        map = list.stream()
                .collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf, LinkedHashSet::new));
        Assert.equals(HashMap.class, map.getClass());
        Assert.equals(new LinkedHashSet<>(Arrays.asList("2", "4")), map.get(Boolean.TRUE));
        Assert.equals(new LinkedHashSet<>(Arrays.asList("1", "3")), map.get(Boolean.FALSE));

        final Map<Boolean, List<String>> map2 = list.stream()
                .collect(CollectorUtil.groupingBy(t -> (t & 1) == 0, String::valueOf));
        Assert.equals(Arrays.asList("2", "2", "4"), map2.get(Boolean.TRUE));
        Assert.equals(Arrays.asList("1", "1", "3"), map2.get(Boolean.FALSE));

    }
}
