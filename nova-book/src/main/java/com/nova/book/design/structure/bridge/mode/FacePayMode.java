package com.nova.book.design.structure.bridge.mode;
/**
 * @description: 人脸
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class FacePayMode implements IPayMode {

    @Override
    public void security() {
        System.out.println("人脸支付");
    }
}
