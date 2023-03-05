package com.nova.book.design.action.observer;

import com.nova.book.design.action.observer.listener.MqEventListener;
import com.nova.book.design.action.observer.listener.MsgEventListener;
import org.junit.jupiter.api.Test;

/**
 * @description: 观察者模式测试类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class AppTest {

    @Test
    public void demoA() {
        MsgEventListener msgEventListener = new MsgEventListener();
        MqEventListener mqEventListener = new MqEventListener();
        // 订阅通知
        EventManager eventManager = new EventManager();
        eventManager.subscribe(msgEventListener);
        eventManager.subscribe(mqEventListener);
        // 通知
        eventManager.notify("来了...");

        System.out.println("----------------");

        // 取消短信订阅
        eventManager.unsubscribe(msgEventListener);
        eventManager.notify("又来了...");
    }

    @Test
    public void demoB(){
        //利用JDK中的Observer进行讲解
        MyObservable observable = new MyObservable();

        //注册观察者
        observable.addObserver((o, arg) -> System.out.println("张三：邮件订阅：" + arg));
        observable.addObserver((o, arg) -> System.out.println("李四：邮件订阅：" + arg));
        observable.addObserver((o, arg) -> System.out.println("王五：邮件订阅：" + arg));
        //调整变化
        observable.setChanged();
        //通知
        observable.notifyObservers("Hello World");
    }

}