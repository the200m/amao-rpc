package com.amao.rpc.core.client;

import com.amao.rpc.core.client.invoker.InJvmInvoker;
import com.amao.rpc.core.client.invoker.RpcInvoker;
import com.amao.rpc.core.config.Config;
import com.amao.rpc.core.exception.RpcException;
import com.amao.rpc.core.util.AssertUtils;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class RpcProxyFactory<T> {

    private RpcClient rpcClient;

    public RpcProxyFactory(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    public RpcProxyFactory() {
        this.rpcClient = new RpcClient();
    }

    public T create(Class<?> interfaceClass) throws RpcException {
        AssertUtils.notNull(interfaceClass, "no interfaceClass");
        Object invoker = null;
        if (Config.INJVM) {
            try {
                invoker = new InJvmInvoker(interfaceClass.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            invoker = new RpcInvoker(rpcClient);
        }
        return (T) Config.reflector.newProxy(interfaceClass, invoker);
    }


    public void close() throws Exception {
        rpcClient.stop();
    }
}
