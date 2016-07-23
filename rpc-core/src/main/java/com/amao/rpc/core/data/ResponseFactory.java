package com.amao.rpc.core.data;

/**
 * Created by 阿毛 on 2016/6/10.
 */

public class ResponseFactory {

    public static Response newHandshakeResponse(Object[] body, String sessionId) {
        Response response = new Response();
        MessageHeader messageHeader = new MessageHeader(sessionId);
        messageHeader.setType(MessageType.HANDSHAKE_ACK.getValue());
        response.setMessageHeader(messageHeader);
        response.setBody(body);
        return response;
    }

    public static Response newHeartBeatResponse(String sessionId) {
        Response response = new Response();
        MessageHeader messageHeader = new MessageHeader(sessionId);
        messageHeader.setType(MessageType.HEARTBEAT_ACK.getValue());
        response.setMessageHeader(messageHeader);
        return response;
    }

    public static Response newBizResponse(String sessionId, Object[] body) {
        Response response = new Response();
        MessageHeader messageHeader = new MessageHeader(sessionId);
        messageHeader.setType(MessageType.BIZ_ACK.getValue());
        response.setMessageHeader(messageHeader);
        response.setBody(body);
        return response;
    }


}
