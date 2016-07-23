package com.amao.rpc.support.boot;


import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 阿毛 on 2016/7/5.
 */
public class RpcServerBoot {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("RpcXmlConfiguration.xml");
    }
}
