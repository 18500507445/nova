package com.nova.book.design.action.mediator;

/**
 * @description: 聊天室 中介类
 * @author: wzh
 * @date: 2022/12/31 8:24
 */
public class ChatRoom {

    public static void showMessage(User user, String message) {
        System.err.printf("[%s]: %s%n", user.getName(), message);
    }
}