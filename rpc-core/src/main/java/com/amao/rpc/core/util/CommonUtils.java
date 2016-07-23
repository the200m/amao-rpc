package com.amao.rpc.core.util;

import java.net.InetSocketAddress;

/**
 * Created by 阿毛 on 2016/6/28.
 */
public class CommonUtils {

    public static String convert2AddressString(InetSocketAddress address) {
        return address.getHostString() + ":" + address.getPort();
    }

    public static InetSocketAddress convert2Address(String netAddressString) {
        String[] arr = netAddressString.split(":");
        return new InetSocketAddress(arr[0], Integer.valueOf(arr[1]));
    }
}
