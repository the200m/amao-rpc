package com.amao.rpc.support.example;


import com.amao.rpc.support.annotation.RpcService;

import java.util.Date;

/**
 * Created by 阿毛 on 2016/6/7.
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String content, Date date) {
        return "hello " + content + "in " + date.toString();
    }
}
