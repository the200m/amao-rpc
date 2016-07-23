package com.amao.rpc.core.task;

import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.client.future.RpcFutureContainer;
import com.amao.rpc.core.data.MessagePacket;
import com.amao.rpc.core.data.Response;
import com.amao.rpc.core.data.ResponseFactory;

/**
 * Created by 阿毛 on 2016/7/7.
 */
public class SendRequestTask extends AbstractSendTask {

    private MessagePacket messagePacket;
    private RpcFutureContainer rpcFutureContainer;

    public SendRequestTask(RpcChannel rpcChannel, MessagePacket messagePacket, RpcFutureContainer rpcFutureContainer) {
        super(rpcChannel);
        this.messagePacket = messagePacket;
        this.rpcFutureContainer = rpcFutureContainer;
    }

    protected MessagePacket createMessagePacket() {
        return messagePacket;
    }

    @Override
    public void onSendFail() {
        Response response = ResponseFactory.newBizResponse(messagePacket.getMessageHeader().getSessionId(), null);
                response.setResultType(Response.RESULT_TYPE_SEND_FAIL);
                rpcFutureContainer.done(response);
    }
}
