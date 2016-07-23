package com.amao.rpc.example.example;



import java.util.Date;
import java.util.Random;

/**
 * Created by 阿毛 on 2016/6/7.
 */

public class TimeServiceImpl implements TimeService {

    @Override
    public String getTime() {
        int ret = new Random().nextInt(10000);
        return new Date().toString() + "||" + ret;
    }
}
