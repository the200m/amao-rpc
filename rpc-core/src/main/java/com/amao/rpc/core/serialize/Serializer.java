package com.amao.rpc.core.serialize;

import com.amao.rpc.core.data.MessagePacket;

import java.io.IOException;

/**
 * Created by 阿毛 on 2016/6/16.
 */
public interface Serializer {

    byte[] pack(MessagePacket messagePacket) throws IOException;

    MessagePacket unpack(byte[] bytes) throws IOException;



}
