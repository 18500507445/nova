package com.nova.rpc.websocket.controller;

import com.nova.rpc.websocket.enums.MsgTypeEnum;
import com.nova.rpc.websocket.message.ChatMsgMessage;
import com.nova.rpc.websocket.utils.WebSocketContextUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @description: Controller
 * @author: wzh
 * @date: 2022/1/7 20:13
 */
@Controller
@RequestMapping("/socket")
public class WebSocketController {

    /**
     * 测试发送消息
     *
     * @param str 消息
     */
    @GetMapping("/send/{str}")
    public void send(@PathVariable("str") String str) {
        ChatMsgMessage chatMsgMessage = new ChatMsgMessage().setMsg(str);
        WebSocketContextUtil.broadcast(MsgTypeEnum.CHAT_MSG.getCode(), chatMsgMessage, null);
    }

    @GetMapping("/")
    public ModelAndView test() {
        return new ModelAndView("index.html");
    }
}
