package benchmark;

import com.amao.rpc.core.client.RpcClient;
import com.amao.rpc.core.client.RpcProxyFactory;
import com.amao.rpc.core.server.ServiceDictionary;
import com.amao.rpc.core.data.ServiceMeta;
import com.google.common.collect.Lists;
import com.amao.rpc.core.example.HelloService;
import com.amao.rpc.core.example.TimeService;
import com.amao.rpc.core.exception.RpcException;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by 阿毛 on 2016/6/7.
 */
public class BenchmarkTest {

    public static void main(String[] args) throws Exception {
        int threadNum = 10;
        final int requestNum = 20;
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        Thread[] threads = new Thread[threadNum];

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

        final RpcProxyFactory rpcProxy = new RpcProxyFactory<>(rpcClient);

        long startTime = System.currentTimeMillis();


        for (int i = 0; i < threadNum; ++i) {
            if (i % 2 == 0) {
                threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < requestNum; i++) {
                            TimeService timeService = null;
                            try {
                                timeService = (TimeService) rpcProxy.create(TimeService.class);
                            } catch (RpcException e) {
                                e.printStackTrace();
                            }
                            String time = timeService.getTime();
                            System.out.println(Thread.currentThread().getId() + ":" + time);
                        }
                        countDownLatch.countDown();
                    }
                });
            } else {
                threads[i] = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < requestNum; i++) {
                            HelloService helloService = null;
                            try {
                                helloService = (HelloService) rpcProxy.create(HelloService.class);
                            } catch (RpcException e) {
                                e.printStackTrace();
                            }
                            String str = helloService.hello(String.valueOf(i), new Date());
                            System.out.println(Thread.currentThread().getId() + ":" + str);
                        }
                        System.out.println(Thread.currentThread().getId() + "===>over");
                        countDownLatch.countDown();
                    }
                });
            }

            threads[i].start();
        }


        countDownLatch.await(30, TimeUnit.SECONDS);

        System.out.println("count:" + countDownLatch.getCount());
        long timeCost = (System.currentTimeMillis() - startTime);
        String msg = String.format("Async future total-time-cost:%sms, req/s=%s", timeCost, ((double) (requestNum * threadNum)) / timeCost * 1000);
        System.out.println(msg);
        rpcProxy.close();
        System.out.println("=====over=====");
    }
}
