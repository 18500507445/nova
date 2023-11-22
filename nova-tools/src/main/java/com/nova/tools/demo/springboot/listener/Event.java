package com.nova.tools.demo.springboot.listener;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * @author: wzh
 * @description 事件
 * @date: 2023/11/22 16:52
 */
@Setter
@Getter
public class Event<T> extends ApplicationEvent {

    private Integer id;

    private T t;

    public Event(Integer id, T t) {
        super(t);
        this.id = id;
        this.t = t;
    }


}
