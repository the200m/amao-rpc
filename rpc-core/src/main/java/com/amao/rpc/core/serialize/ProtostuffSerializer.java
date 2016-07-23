package com.amao.rpc.core.serialize;

import com.amao.rpc.core.data.MessagePacket;

import java.io.IOException;

public class ProtostuffSerializer implements Serializer {

    @Override
    public byte[] pack(MessagePacket messagePacket) throws IOException {
        return ProtostuffUtil.serialize(messagePacket);
    }

    @Override
    public MessagePacket unpack(byte[] bytes) throws IOException {
        return ProtostuffUtil.deserialize(bytes, MessagePacket.class);
    }



}
