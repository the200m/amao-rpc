package com.amao.rpc.core.client.invoker;


import com.amao.rpc.core.client.RpcClient;
import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.client.channel.RpcChannelManager;
import com.amao.rpc.core.client.future.RpcFuture;
import com.amao.rpc.core.client.future.RpcFutureContainer;
import com.amao.rpc.core.config.Config;
import com.amao.rpc.core.data.Request;
import com.amao.rpc.core.data.RequestFactory;
import com.amao.rpc.core.data.Response;
import com.amao.rpc.core.data.ServiceMeta;
import com.amao.rpc.core.exception.RpcException;
import com.amao.rpc.core.task.SendRequestTask;
import com.amao.rpc.core.util.ClassUtils;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class RpcInvoker implements InvocationHandler, MethodInterceptor {

    private RpcChannelManager channelPool;
    private RpcFutureContainer futureContainer;
    private Executor executor;

    public RpcInvoker(RpcClient rpcClient) {
        channelPool = rpcClient.getChannelPool();
        futureContainer = rpcClient.getRpcFutureContainer();
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws RpcException {
        return getObject(method, args);
    }

    private Object getObject(Method method, Object[] args) throws RpcException {
        ServiceMeta serviceMetadata = new ServiceMeta();
        serviceMetadata.setServiceName(ClassUtils.getServiceName(method));
        RpcChannel rpcChannel = channelPool.getRpcChannel(serviceMetadata);
        return getRequest(method, args, rpcChannel);
    }

    private Object getRequest(Method method, Object[] args, RpcChannel rpcChannel) {
        final Request request = RequestFactory.newBizRequest(method, args);
        final RpcFuture rpcFuture = new RpcFuture(request);
        futureContainer.pend(request.getMessageHeader().getSessionId(), rpcFuture);

        SendRequestTask task = new SendRequestTask(rpcChannel, request, futureContainer);
        executor.execute(task);

        if (rpcFuture.getState() == RpcFuture.STATE_PENDING) {
            Response response = null;
            try {
                response = rpcFuture.get(Config.RESULT_WAIT_TIMEOUT_SECOND, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
            if (response != null) {
                try {
                    return response.getResult();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return null;
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return getObject(method, args);
    }

}
