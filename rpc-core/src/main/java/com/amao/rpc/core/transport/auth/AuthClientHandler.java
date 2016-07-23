package com.amao.rpc.core.transport.auth;

import com.amao.rpc.core.data.*;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 阿毛 on 2016/6/21.
 */
public class AuthClientHandler extends ChannelHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(AuthClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Request handshakeRequest = RequestFactory.newHandshakeRequest();
        ctx.writeAndFlush(handshakeRequest);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = new Response((MessagePacket) msg);
        if (response.getMessageHeader().getType() == MessageType.HANDSHAKE_ACK.getValue()) {
            Integer authResult = (Integer) response.getBody()[0];
            if (authResult == AuthResultType.AUTH_OK.getValue()) {
                super.channelRead(ctx, msg);
            } else {
                logger.warn("auth failed!");
                ctx.close();
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }
}
