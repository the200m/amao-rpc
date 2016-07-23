package base;


import com.amao.rpc.core.server.RpcExporter;
import com.amao.rpc.core.server.RpcServer;
import com.amao.rpc.core.example.HelloServiceImpl;
import com.amao.rpc.core.example.TimeServiceImpl;
import com.amao.rpc.core.registry.ServiceRegistry;
import com.amao.rpc.core.registry.ZookeeperServiceRegistry;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 阿毛 on 2016/6/7.
 */
public class ServerMain {


    public static void main(String[] args) {

        final ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(new InetSocketAddress("121.41.76.228", 2181));

        final Map<String, Object> serviceBeanMap = new HashMap<>();
        serviceBeanMap.put("com.amao.rpc.core.example.TimeService", new TimeServiceImpl());
        serviceBeanMap.put("com.amao.rpc.core.example.HelloService", new HelloServiceImpl());

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 34568);
                RpcServer rpcServer1 = new RpcServer();
                RpcExporter rpcExporter = new RpcExporter(serverAddress, serviceRegistry, rpcServer1);
                rpcExporter.mockRegisterBean(serviceBeanMap);
                try {
                    rpcExporter.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 34569);
                RpcServer rpcServer1 = new RpcServer();
                RpcExporter rpcExporter = new RpcExporter(serverAddress, serviceRegistry, rpcServer1);
                rpcExporter.mockRegisterBean(serviceBeanMap);
                try {
                    rpcExporter.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 34570);
                RpcServer rpcServer1 = new RpcServer();
                RpcExporter rpcExporter = new RpcExporter(serverAddress, serviceRegistry, rpcServer1);
                rpcExporter.mockRegisterBean(serviceBeanMap);
                try {
                    rpcExporter.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

    }
}
