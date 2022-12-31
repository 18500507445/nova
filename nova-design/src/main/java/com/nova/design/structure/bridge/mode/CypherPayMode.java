package com.nova.design.structure.bridge.mode;

/**
 * @description: 密码
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class CypherPayMode implements IPayMode {

    @Override
    public void security() {
        System.out.println("密码支付");
    }
}
