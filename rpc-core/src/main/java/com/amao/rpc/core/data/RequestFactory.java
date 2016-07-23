package com.amao.rpc.core.data;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by 阿毛 on 2016/6/10.
 */

public class RequestFactory {

    public static Request newHandshakeRequest() {
        Request request = new Request();
        MessageHeader messageHeader = new MessageHeader(UUID.randomUUID().toString());
        messageHeader.setType(MessageType.HANDSHAKE_REQ.getValue());
        request.setMessageHeader(messageHeader);
        return request;
    }

    public static Request newHeartBeatRequest() {
        Request request = new Request();
        MessageHeader messageHeader = new MessageHeader(UUID.randomUUID().toString());
        messageHeader.setType(MessageType.HEARTBEAT_REQ.getValue());
        request.setMessageHeader(messageHeader);
        return request;
    }

    public static Request newBizRequest(Method method, Object[] args) {
        Request request = new Request();
        MessageHeader messageHeader = new MessageHeader(UUID.randomUUID().toString());
        messageHeader.setType(MessageType.BIZ_REQ.getValue());
        messageHeader.setInterfaceName(method.getDeclaringClass().getName());
        messageHeader.setMethodName(method.getName());
        request.setMessageHeader(messageHeader);
        request.setBody(args);
        return request;
    }
}
