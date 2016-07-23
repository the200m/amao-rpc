package com.amao.rpc.core.config;

import com.amao.rpc.core.client.channel.connector.Connector;
import com.amao.rpc.core.client.channel.connector.DefaultConnector;
import com.amao.rpc.core.load.LoadBalancer;
import com.amao.rpc.core.load.RoundLoadBalancer;
import com.amao.rpc.core.method.interception.JdkReflector;
import com.amao.rpc.core.method.interception.Reflector;
import com.amao.rpc.core.serialize.ProtostuffSerializer;
import com.amao.rpc.core.serialize.Serializer;

/**
 * Created by 阿毛 on 2016/6/16.
 */
public class Config {
    public static final int RESULT_WAIT_TIMEOUT_SECOND = 3;

    public static final boolean INJVM = true;

    //========================================================================
//    private static Serializer serializer = new MessagePackSerializer();
    // private static Serializer serializer = new ProtostuffSerializer();
    public static Serializer serializer = new ProtostuffSerializer();

    //public static Reflector reflector = new CGlibReflector();
    public static Reflector reflector = new JdkReflector();

    public static Connector connector = new DefaultConnector();

    public static LoadBalancer loadBalancer = new RoundLoadBalancer();
    //=======================================================================


}
