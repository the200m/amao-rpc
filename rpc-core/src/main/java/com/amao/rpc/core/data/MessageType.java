package com.amao.rpc.core.data;

/**
 * Created by 阿毛 on 2016/6/21.
 */
public enum MessageType {

    BIZ_REQ(1),
    BIZ_ACK(2),
    BIZ_ONE_WAY(3),
    HANDSHAKE_REQ(4),
    HANDSHAKE_ACK(5),
    HEARTBEAT_REQ(6),
    HEARTBEAT_ACK(7);


    MessageType(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

}
