package com.nova.book.effectivejava.chapter4.section6;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: wzh
 * @date: 2023/2/22 14:20
 */
class SectionTest6 {

    @Test
    public void demoA() {
        Map<Object, Object> hashMap = new HashMap<>(16);
        hashMap.put("1", 1);
        hashMap.put("2", "2");

        Integer i = (Integer) hashMap.get("1");
        Integer j = (Integer) hashMap.get("2");
    }

    @Test
    public void demoB() {
        InstanceContainer.putInstance(SectionTest6.class, new SectionTest6());

        SectionTest6 instance = InstanceContainer.getInstance(SectionTest6.class);

    }


}
