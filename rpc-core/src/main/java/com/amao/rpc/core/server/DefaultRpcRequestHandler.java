package com.amao.rpc.core.server;

import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.data.Request;
import com.amao.rpc.core.task.HandleRequestTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by 阿毛 on 2016/7/7.
 */
public class DefaultRpcRequestHandler implements RpcRequestHandler {

    private Executor executor;

    public DefaultRpcRequestHandler() {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    @Override
    public void handle(RpcChannel rpcChannel, Request request) {
        HandleRequestTask task = new HandleRequestTask(rpcChannel, request);
        executor.execute(task);
    }


}
