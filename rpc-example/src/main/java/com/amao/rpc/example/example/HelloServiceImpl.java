package com.amao.rpc.example.example;

import java.util.Date;

/**
 * Created by 阿毛 on 2016/6/7.
 */

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String content, Date date) {
        return "hello " + content + "in " + date.toString();
    }
}
