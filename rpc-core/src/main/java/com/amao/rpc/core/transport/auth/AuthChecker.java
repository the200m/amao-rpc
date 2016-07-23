package com.amao.rpc.core.transport.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 阿毛 on 2016/6/21.
 */
public class AuthChecker {

    private Map<String, Boolean> whileList = new ConcurrentHashMap<>();

    public AuthChecker() {
        whileList.put("127.0.0.1", true);
    }

    public boolean check(String key) {
        return whileList.containsKey(key);
    }

}
