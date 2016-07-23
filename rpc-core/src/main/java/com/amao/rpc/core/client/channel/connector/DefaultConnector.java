package com.amao.rpc.core.client.channel.connector;


import com.amao.rpc.core.client.RpcClient;
import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.exception.ConnectFailedException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by 阿毛 on 2016/6/26.
 */
public class DefaultConnector implements Connector {

    @Override
    public String getName() {
        return "Round";
    }

    @Override
    public RpcChannel connect(RpcClient rpcClient, List<InetSocketAddress> providerAddressList) throws ConnectFailedException {
        RpcChannel rpcChannel;
        for (InetSocketAddress address : providerAddressList) {
            rpcChannel = rpcClient.connect(address);
            if (rpcChannel != null) {
                return rpcChannel;
            }
        }
        throw new ConnectFailedException("no valid channel");
    }
}
