package com.nova.book.design.action.mediator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 用户实体类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;

    public void sendMessage(String message) {
        ChatRoom.showMessage(this, message);
    }
}