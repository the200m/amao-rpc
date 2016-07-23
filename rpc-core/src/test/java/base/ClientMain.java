package base;


import com.amao.rpc.core.client.RpcClient;
import com.amao.rpc.core.client.RpcProxyFactory;
import com.amao.rpc.core.server.ServiceDictionary;
import com.amao.rpc.core.data.ServiceMeta;
import com.google.common.collect.Lists;
import com.amao.rpc.core.example.HelloService;
import com.amao.rpc.core.exception.RpcException;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;

/**
 * Created by 阿毛 on 2016/6/7.
 */
public class ClientMain {

    public static void main(String[] args) throws Exception {
        InetSocketAddress registryAddress = new InetSocketAddress("121.41.76.228", 2181);
        ServiceDictionary serviceDictionary = ServiceDictionary.getInstance();

        List<ServiceMeta> serviceList = Lists.newArrayList();
        ServiceMeta serviceMeta1 = new ServiceMeta();
        serviceMeta1.setServiceName("com.amao.rpc.core.example.TimeService");
        ServiceMeta serviceMeta2 = new ServiceMeta();
        serviceMeta2.setServiceName("com.amao.rpc.core.example.HelloService");
        serviceList.add(serviceMeta1);
        serviceList.add(serviceMeta2);
        serviceDictionary.discoverService(serviceList, registryAddress);


        RpcClient rpcClient = new RpcClient();
        rpcClient.getChannelPool().initChannels(serviceList);

        RpcProxyFactory rpcProxy = new RpcProxyFactory<>(rpcClient);
        HelloService helloService = null;
        try {
            helloService = (HelloService) rpcProxy.create(HelloService.class);
        } catch (RpcException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 20; i++) {
            String str = helloService.hello("cc", new Date());
            System.out.println(i + "|str:" + str);
        }

        rpcProxy.close();
        System.out.println("=====over=====");
    }
}
