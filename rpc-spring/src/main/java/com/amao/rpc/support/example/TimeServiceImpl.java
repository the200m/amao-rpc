package com.amao.rpc.support.example;

import com.amao.rpc.support.annotation.RpcService;

import java.util.Date;
import java.util.Random;

/**
 * Created by 阿毛 on 2016/6/7.
 */
@RpcService(TimeService.class)
public class TimeServiceImpl implements TimeService {

    @Override
    public String getTime() {
        int ret = new Random().nextInt(10000);
        return new Date().toString() + "||" + ret;
    }
}
