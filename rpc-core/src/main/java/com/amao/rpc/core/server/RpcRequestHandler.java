package com.amao.rpc.core.server;

import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.data.Request;

/**
 * Created by 阿毛 on 2016/7/7.
 */
public interface RpcRequestHandler {

    void handle(RpcChannel rpcChannel, Request request);

}
