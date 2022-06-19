package com.nova.tools.vc.tps.fhyz;

import java.math.BigDecimal;

/**
 * @author wangzehui
 * @date 2018/10/16 16:16
 */

public class Item {
    private String name;
    private int count;
    private BigDecimal price;

    public Item(String name, int count, BigDecimal price) {
        this.name = name;
        this.count = count;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
