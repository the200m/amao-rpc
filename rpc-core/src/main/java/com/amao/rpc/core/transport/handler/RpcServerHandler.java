package com.amao.rpc.core.transport.handler;

import com.amao.rpc.core.client.channel.RpcChannel;
import com.amao.rpc.core.data.MessagePacket;
import com.amao.rpc.core.data.MessageType;
import com.amao.rpc.core.data.Request;
import com.amao.rpc.core.server.DefaultRpcRequestHandler;
import com.amao.rpc.core.server.RpcRequestHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * Created by 阿毛 on 2016/6/21.
 */
public class RpcServerHandler extends ChannelHandlerAdapter {

    private RpcRequestHandler rpcRequestHandler;

    public RpcServerHandler() {
        rpcRequestHandler = new DefaultRpcRequestHandler();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = new Request((MessagePacket) msg);
        if (request.getMessageHeader().getType() == MessageType.BIZ_REQ.getValue()) {
            RpcChannel channel = new RpcChannel(ctx.channel());
            rpcRequestHandler.handle(channel, request);
        } else {
            super.channelRead(ctx, msg);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
