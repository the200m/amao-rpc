package com.amao.rpc.core.client.future;

import com.amao.rpc.core.data.Response;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 阿毛 on 2016/6/26.
 */
public class RpcFutureContainer {

    private ConcurrentHashMap<String, RpcFuture> pendingFutures = new ConcurrentHashMap<>();

    public void pend(String sessionId, RpcFuture future) {
        future.setState(RpcFuture.STATE_PENDING);
        pendingFutures.put(sessionId, future);
    }


    public void done(Response response) {
        String id = response.getMessageHeader().getSessionId();
        RpcFuture future = pendingFutures.get(id);
        if (future != null) {
            pendingFutures.remove(id);
            future.done(response);
        }
    }


}
