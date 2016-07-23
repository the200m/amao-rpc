package com.amao.rpc.core.data;

import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * Created by 阿毛 on 2016/6/10.
 */
@Message
public class Response extends MessagePacket implements Serializable {

    public final static int RESULT_TYPE_COMMON = 1;
    public final static int RESULT_TYPE_ERR = 2;
    public final static int RESULT_TYPE_SEND_FAIL = 3;

    private int resultType = RESULT_TYPE_COMMON;

    public Response(MessagePacket messagePacket) {
        super(messagePacket);
    }

    public Response() {
    }

    public Object getResult() throws Throwable {
        if (resultType == RESULT_TYPE_COMMON) {
            return getBody()[0];
        } else if (resultType == RESULT_TYPE_ERR) {
            Throwable throwable = (Throwable) getBody()[0];
            throw throwable;
        }
        return null;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }
}
