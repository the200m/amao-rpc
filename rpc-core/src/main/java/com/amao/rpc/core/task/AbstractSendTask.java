package com.amao.rpc.core.task;

import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.client.channel.SendListener;
import com.amao.rpc.core.data.MessagePacket;

/**
 * Created by 阿毛 on 2016/7/7.
 */
public abstract class AbstractSendTask implements Runnable, SendListener {
    private RpcChannel rpcChannel;


    public AbstractSendTask(RpcChannel rpcChannel) {
        this.rpcChannel = rpcChannel;

    }

    @Override
    public void run() {
        MessagePacket messagePacket = createMessagePacket();
        rpcChannel.send(messagePacket, this);
    }

    abstract protected MessagePacket createMessagePacket();
}
