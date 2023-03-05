package com.nova.book.design.structure.adapter;

/**
 * @description: 对象适配器 通过包装一个需要适配的对象，把原接口转换成目标接口
 * @author: wzh
 * @date: 2022/12/31 10:38
 */
public class ObjAdapter implements UsbTarget {

    private TypeCAdapter typeCAdapter;

    public ObjAdapter(TypeCAdapter typeCAdapter) {
        this.typeCAdapter = typeCAdapter;
    }

    @Override
    public void usb() {
        // 拿到typeC数据 做下转换处理...
        this.typeCAdapter.typeC();
        // ...

        System.out.println("typeC -> usb");
    }
}
