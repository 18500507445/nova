package com.nova.tools.vc.tps.fhyz;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangzehui
 * @date 2018/10/16 16:30
 */

public class SeatListTest {

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>(Arrays.asList(
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 20, new BigDecimal("19.99")),
                new Item("orange", 10, new BigDecimal("29.99")),
                new Item("watermelon", 10, new BigDecimal("29.99")),
                new Item("papaya", 20, new BigDecimal("9.99")),
                new Item("apple", 10, new BigDecimal("9.99")),
                new Item("banana", 10, new BigDecimal("19.99")),
                new Item("apple", 20, new BigDecimal("9.99"))
        ));

        //分组
        Map<String, List<Item>> grouped = items.stream().collect(Collectors.groupingBy(Item::getName));

        Iterator<String> iterator = grouped.keySet().iterator();
        List<Item> itemAllList = new ArrayList<>();
        while (iterator.hasNext()) {
            List<Item> itemList = grouped.get(iterator.next());
            // TODO: 2018/10/16 按列排序  list-1
            // TODO: 2018/10/16 按横坐标排序 list-2
            for (int i = 0; i < itemList.size(); i++) {
                // TODO: 2018/10/16 list-1.get(i).setX(list-2.get(i).getY());
                // TODO: 2018/10/16 list-1.get(i).setY(list-2.get(i).getX());
            }
        }


    }

}
