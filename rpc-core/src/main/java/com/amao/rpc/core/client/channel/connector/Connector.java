package com.amao.rpc.core.client.channel.connector;


import com.amao.rpc.core.client.RpcClient;
import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.exception.ConnectFailedException;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by 阿毛 on 2016/6/26.
 */
public interface Connector {

    String getName();

    RpcChannel connect(RpcClient rpcClient, List<InetSocketAddress> providerAddressList) throws ConnectFailedException;

}
