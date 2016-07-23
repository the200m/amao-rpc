package com.amao.rpc.core.transport.codec;

import com.amao.rpc.core.config.Config;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import com.amao.rpc.core.serialize.Serializer;

import java.util.List;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class RpcDecoder extends MessageToMessageDecoder<ByteBuf> {


    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final int length = in.readableBytes();
        final byte[] array = new byte[length];
        in.getBytes(in.readerIndex(), array, 0, length);
        Serializer serializer = Config.serializer;
        out.add(serializer.unpack(array));
    }

}
