package com.nova.websocket.message;

import com.nova.websocket.enums.MsgTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 聊天室人数
 * @author: wzh
 * @date: 2022/1/7 20:13
 */
@Data
@Accessors(chain = true)
public class ChatCountMessage implements Message {

    public static final String TYPE = MsgTypeEnum.CHAT_COUNT.getCode();

    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 内容
     */
    private Integer count;

}
