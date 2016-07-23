package com.amao.rpc.core.data;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Created by 阿毛 on 2016/6/10.
 */
@Message
public class Request extends MessagePacket implements Serializable{

    public Request(MessagePacket messagePacket) {
        super(messagePacket);
    }

    public Request() {
    }
}
