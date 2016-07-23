package com.amao.rpc.core.transport.codec;

import com.amao.rpc.core.config.Config;
import com.amao.rpc.core.data.MessagePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class RpcEncoder extends MessageToByteEncoder<MessagePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessagePacket messagePacket, ByteBuf out) throws Exception {
        byte[] raw = Config.serializer.pack(messagePacket);
        out.writeBytes(raw);
    }
}
