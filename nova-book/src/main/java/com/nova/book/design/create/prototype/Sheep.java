package com.nova.book.design.create.prototype;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * @description: 羊实体类
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
@Data
public class Sheep implements Cloneable {

    private String name;

    private String color;

    public Sheep friend;

    public Sheep(String name, String color) {
        super();
        this.name = name;
        this.color = color;
    }

    /**
     * 克隆实例
     */
    @Override
    @SneakyThrows(Exception.class)
    protected Object clone() {
        return super.clone();
    }

}
