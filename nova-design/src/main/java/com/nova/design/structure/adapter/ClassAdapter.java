package com.nova.design.structure.adapter;

/**
 * 类适配器
 *
 * @description: 通过包装一个需要适配的对象，把原接口转换成目标接口
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class ClassAdapter extends TypeCAdapter implements UsbTarget {

    @Override
    public void usb() {
        // 拿到typeC数据 做下转换处理...
        super.typeC();
        // ...

        System.out.println("typeC -> usb");
    }
}
