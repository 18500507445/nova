package com.nova.tools.utils.hutool.json;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import lombok.Data;
import org.junit.jupiter.api.Test;

public class TransientTest {

    @Data
    static class Bill {
        private transient String id;
        private String bizNo;
    }

    @Test
    public void beanWithoutTransientTest() {
        Bill detailBill = new Bill();
        detailBill.setId("3243");
        detailBill.setBizNo("bizNo");

        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject jsonObject = new JSONObject(detailBill,
                JSONConfig.create().setTransientSupport(false));
        Assert.equals("{\"id\":\"3243\",\"bizNo\":\"bizNo\"}", jsonObject.toString());
    }

    @Test
    public void beanWithTransientTest() {
        Bill detailBill = new Bill();
        detailBill.setId("3243");
        detailBill.setBizNo("bizNo");

        //noinspection MismatchedQueryAndUpdateOfCollection
        final JSONObject jsonObject = new JSONObject(detailBill,
                JSONConfig.create().setTransientSupport(true));
        Assert.equals("{\"bizNo\":\"bizNo\"}", jsonObject.toString());
    }

    @Test
    public void beanWithoutTransientToBeanTest() {
        Bill detailBill = new Bill();
        detailBill.setId("3243");
        detailBill.setBizNo("bizNo");

        final JSONObject jsonObject = new JSONObject(detailBill,
                JSONConfig.create().setTransientSupport(false));

        final Bill bill = jsonObject.toBean(Bill.class);
        Assert.equals("3243", bill.getId());
        Assert.equals("bizNo", bill.getBizNo());
    }

    @Test
    public void beanWithTransientToBeanTest() {
        Bill detailBill = new Bill();
        detailBill.setId("3243");
        detailBill.setBizNo("bizNo");

        final JSONObject jsonObject = new JSONObject(detailBill,
                JSONConfig.create().setTransientSupport(true));

        final Bill bill = jsonObject.toBean(Bill.class);
        Assert.isNull(bill.getId());
        Assert.equals("bizNo", bill.getBizNo());
    }
}
