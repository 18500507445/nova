package com.nova.tools.utils.hutool.core.clone;


import cn.hutool.core.clone.DefaultCloneable;
import lombok.AllArgsConstructor;
import lombok.Data;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultCloneTest {

    @Test
    public void clone0() {
        Car oldCar = new Car();
        oldCar.setId(1);
        oldCar.setWheelList(Stream.of(new Wheel("h")).collect(Collectors.toList()));

        Car newCar = oldCar.clone0();
        Assert.equals(oldCar.getId(), newCar.getId());
        Assert.equals(oldCar.getWheelList(), newCar.getWheelList());

        newCar.setId(2);
        Assert.notEquals(oldCar.getId(), newCar.getId());
        newCar.getWheelList().add(new Wheel("s"));

        Assert.equals(oldCar, newCar);

    }

    @Data
    static class Car implements DefaultCloneable<Car> {
        private Integer id;
        private List<Wheel> wheelList;
    }

    @Data
    @AllArgsConstructor
    static class Wheel {
        private String direction;
    }

}


