package com.amao.rpc.core.transport.auth;

/**
 * Created by 阿毛 on 2016/6/21.
 */
public enum AuthResultType {

    AUTH_OK(1, "auth ok"),
    AUTH_LOGIN_REPEATED(2, "auth failed,repeated login"),
    AUTH_NOT_IN_WHITE_LIST(3, "auth failed,not in white list");


    AuthResultType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    private int value;

    private String description;

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
