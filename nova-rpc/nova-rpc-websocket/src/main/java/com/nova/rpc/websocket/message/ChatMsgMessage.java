package com.nova.rpc.websocket.message;

import com.nova.rpc.websocket.enums.MsgTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 发送给所有人的群聊消息的 Message
 * @author: wzh
 * @date: 2022/1/7 20:13
 */
@Data
@Accessors(chain = true)
public class ChatMsgMessage implements Message {

    public static final String TYPE = MsgTypeEnum.CHAT_MSG.getCode();

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private String msg;

}
