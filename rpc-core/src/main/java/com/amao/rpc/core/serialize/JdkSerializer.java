package com.amao.rpc.core.serialize;

import com.amao.rpc.core.data.MessagePacket;

import java.io.*;

public class JdkSerializer implements Serializer {


    @Override
    public byte[] pack(MessagePacket messagePacket) {
        byte[] bytes = null;
        try (ByteArrayOutputStream bo = new ByteArrayOutputStream();
             ObjectOutputStream oo = new ObjectOutputStream(bo)) {
            oo.writeObject(messagePacket);
            bytes = bo.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    @Override
    public MessagePacket unpack(byte[] bytes) {
        MessagePacket messagePacket = null;
        try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            messagePacket = (MessagePacket) oi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messagePacket;
    }




}
