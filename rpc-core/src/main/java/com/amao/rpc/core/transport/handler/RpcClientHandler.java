package com.amao.rpc.core.transport.handler;

import com.amao.rpc.core.client.future.RpcFutureContainer;
import com.amao.rpc.core.data.MessagePacket;
import com.amao.rpc.core.data.MessageType;
import com.amao.rpc.core.data.Response;
import com.amao.rpc.core.util.AssertUtils;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


/**
 * Created by 阿毛 on 2016/6/21.
 */
public class RpcClientHandler extends ChannelHandlerAdapter {

    private RpcFutureContainer rpcFutureContainer;

    public RpcClientHandler(RpcFutureContainer rpcFutureContainer) {
        AssertUtils.notNull(rpcFutureContainer, "no rpcFutureContainer");
        this.rpcFutureContainer = rpcFutureContainer;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        Response response = new Response((MessagePacket) msg);
        if (response.getMessageHeader().getType() == MessageType.BIZ_ACK.getValue()) {
            rpcFutureContainer.done(response);
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
