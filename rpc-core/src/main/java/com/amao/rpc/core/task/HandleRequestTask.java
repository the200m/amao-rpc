package com.amao.rpc.core.task;

import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.config.Config;
import com.amao.rpc.core.data.MessageHeader;
import com.amao.rpc.core.data.MessagePacket;
import com.amao.rpc.core.data.Response;
import com.amao.rpc.core.data.ResponseFactory;
import com.amao.rpc.core.method.MethodRef;
import com.amao.rpc.core.method.MethodsHolder;

/**
 * Created by 阿毛 on 2016/7/7.
 */
public class HandleRequestTask extends AbstractSendTask {

    private MessagePacket messagePacket;

    public HandleRequestTask(RpcChannel rpcChannel, MessagePacket messagePacket) {
        super(rpcChannel);
        this.messagePacket = messagePacket;
    }

    protected MessagePacket createMessagePacket() {
        Object[] result = new Object[1];
        MessageHeader header = messagePacket.getMessageHeader();
        Response response = ResponseFactory.newBizResponse(header.getSessionId(), result);
        Object obj = null;
        try {
            String interfaceName = header.getInterfaceName();
            String methodName = header.getMethodName();
            MethodRef methodRef = MethodsHolder.getMethodRef(interfaceName, methodName);
            obj = Config.reflector.invoke(methodRef, messagePacket.getBody());
        } catch (Exception e) {
            result[0] = e.getCause();
            response.setResultType(Response.RESULT_TYPE_ERR);
        }
        result[0] = obj;
        return response;
    }

    @Override
    public void onSendFail() {

    }
}
