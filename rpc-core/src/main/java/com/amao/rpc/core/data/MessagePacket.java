package com.amao.rpc.core.data;

import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by 阿毛 on 2016/6/20.
 */
@Message
public class MessagePacket implements Serializable{

    private MessageHeader messageHeader;
    private Object[] body;

    public MessagePacket(MessagePacket messagePacket) {
        this.messageHeader = messagePacket.messageHeader;
        this.body = messagePacket.body;
    }

    public MessagePacket() {

    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Object[] getBody() {
        return body;
    }

    public void setBody(Object[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MessagePacket{" +
                "messageHeader=" + messageHeader +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
